package ru.khav.ProjectNIC.Controllers;

import ru.khav.ProjectNIC.MainWindow;
import ru.khav.ProjectNIC.utill.ButNames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchSeq extends AbstractAction {
    MainWindow mainWindow;

    public SearchSeq(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        System.out.println("Нажатие на кнопку <" + btn.getName() + ">");

        if (btn.getName().equalsIgnoreCase(ButNames.Search.name())) {
            List<String> data = mainWindow.getCurrentStrData();
            String seq = mainWindow.getSearchSeq().getText();
            int cols = MainWindow.getCountBytee();

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
                                mainWindow.clearHighlightRanges();//убираем старые выделенные
                                for (List<Integer> r : part) {
                                    mainWindow.addHighlightRange(r.get(0), r.get(1), r.get(2), r.get(3));
                                }
                                mainWindow.getTableData().repaint();
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

        if (btn.getName().equalsIgnoreCase(ButNames.DelHighlights.name())) {
            mainWindow.clearHighlightRanges();
        }
    }
}

