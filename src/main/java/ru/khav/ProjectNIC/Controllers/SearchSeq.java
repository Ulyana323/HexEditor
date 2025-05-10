package ru.khav.ProjectNIC.Controllers;
import ru.khav.ProjectNIC.MainWindow;

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

        if (btn.getName().equalsIgnoreCase("tosearch")) {
            List<String> data = mainWindow.getCurrentStrData();
            String seq = mainWindow.getSearchSeq().getText();
            int cols = mainWindow.getCountBytee();

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

                            int sr = j / cols, sc = j % cols;
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
                                    mainWindow.addHighlightRange(part.get(0).get(0), part.get(0).get(1), part.get(0).get(2), part.get(0).get(3));
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
           /* try {
                int sc,sr,ec,er;
                List cur = mainWindow.getCurrentStrData();
                String[] seq = mainWindow.getSearchSeq().getText().split("-");
                for (int i = 0; i < cur.size()+2; i++) {
                    if (cur.get(i).equals(seq[0]))
                    {
                        //начальные и конечные ряд и столбец диапазона выделения
                        sr=i/mainWindow.getCountBytee();
                        sc=i%mainWindow.getCountBytee();
                        er=(i+2)/mainWindow.getCountBytee();
                        ec=(i+2)%mainWindow.getCountBytee();
                        if (cur.get(i+1).equals(seq[1]))
                        {
                            if (cur.get(i+2).equals(seq[2]))
                            {
                                System.out.println("YES");
                                mainWindow.setStartCol(sc);
                                mainWindow.setEndCol(ec);
                                mainWindow.setStartRow(sr);
                                mainWindow.setEndRow(er);
                                mainWindow.getTableData().repaint();
                                break;
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
*/
        if (btn.getName().equalsIgnoreCase("delcolor")) {
            mainWindow.clearHighlightRanges();
        }
    }
}

