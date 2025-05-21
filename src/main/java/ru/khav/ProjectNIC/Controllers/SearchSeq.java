package ru.khav.ProjectNIC.Controllers;

import ru.khav.ProjectNIC.UI_Components.PanelFactory;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.utill.DataLoaderToTables;
import ru.khav.ProjectNIC.utill.DataManager;
import ru.khav.ProjectNIC.utill.Globals;
import ru.khav.ProjectNIC.views.MainWindow;
import ru.khav.ProjectNIC.utill.ButtonNames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class SearchSeq extends AbstractAction {
    MainWindow mainWindow;
    TableFactory tableFactory;
    DataManager dataManager;
    PanelFactory panelFactory;
    DataLoaderToTables dataLoaderToTables;


    public SearchSeq(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow=mainWindow;
        panelFactory=mainWindow.getPanelFactory();
        dataManager=mainWindow.getDataManager();
        tableFactory=mainWindow.getTableFactory();
        dataLoaderToTables=mainWindow.getDataLoaderToTables();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase(ButtonNames.Search.name())) {
            List<String> data = dataManager.getCurrentStrData();
            String seq = panelFactory.getSearchSeq().getText();
            int cols = Globals.countByte;

            String[] pattern = seq.split("-");
            int processors = Runtime.getRuntime().availableProcessors();//сколько потоков мы можем создать
            int chunkSize = data.size() / processors;//сколько элементов будет обрабатывать каждый поток

            ExecutorService executor = Executors.newCachedThreadPool();
            CompletionService<List<List<Integer>>> que = new ExecutorCompletionService<>(executor);// очередь задач

            for (int i = 0; i < processors; i++) {
                int start = i * chunkSize;
                int end = (i == processors - 1) ? data.size() : start + chunkSize /*+ pattern.length*/;

                que.submit(() -> {//каждый раз создав Future
                    List<List<Integer>> result = new ArrayList<>();
                    for (int j = start; j < Math.min(end, data.size() - pattern.length + 1); j++) {
                        if (data.get(j).equals(pattern[0]) &&
                                data.get(j + 1).equals(pattern[1]) &&
                                data.get(j + 2).equals(pattern[2])) {

                            int sr = j / cols, sc = j % cols;//(sr/c - start row/col) (er/c - end row/col)
                            int er = (j + 2) / cols, ec = (j + 2) % cols;
                            result.add(Arrays.asList(sr, sc, er, ec));
                        }
                    }
                    return result;
                });
            }

            //новый поток для обработки результатов
            new Thread(() -> {
                List<List<Integer>> allResults = new ArrayList<>();
                for (int i = 0; i < processors; i++) {
                    try {
                        List<List<Integer>> part = que.take().get();
                        if (!part.isEmpty()) {
                            SwingUtilities.invokeLater(() -> {
                                tableFactory.clearHighlightRanges();//убираем старые выделенные
                                for (List<Integer> r : part) {
                                    tableFactory.addHighlightRange(r.get(0), r.get(1), r.get(2), r.get(3));
                                }
                                tableFactory.getTableData().repaint();
                            });
                            allResults.addAll(part);
                        }
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
                executor.shutdown();
            }).start();
        }

        if (btn.getName().equalsIgnoreCase(ButtonNames.DelHighlights.name())) {
            tableFactory.clearHighlightRanges();
        }
        if (btn.getName().equalsIgnoreCase(ButtonNames.Info.name())) {
            JOptionPane.showMessageDialog(mainWindow,"ctrl+C - копировать без вырезки\n" +
                            "ctrl+B - вырезать c обнулением\n"+
                    "ctrl+V - вставить с заменой\n"+
                    "ctrl+X - вставить без замены со сдвигом (только на последней странице)\n",
                    "горячие клавиши", INFORMATION_MESSAGE);
        }
    }
}

