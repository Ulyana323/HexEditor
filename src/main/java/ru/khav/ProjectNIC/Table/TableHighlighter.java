package ru.khav.ProjectNIC.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableHighlighter {
    private List<List<Integer>> highlightRanges = new ArrayList<>();

    public TableCellRenderer getCustomRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                boolean highlightFound = highlightRanges.stream().anyMatch(
                        range -> row >= range.get(0) && row <= range.get(2)
                                && column >= range.get(1) && column <= range.get(3)
                );

                if (highlightFound) {
                    c.setBackground(Color.CYAN);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }
                return c;
            }
        };
    }

    public void addHighlightRange(int sr, int sc, int er, int ec) {
        highlightRanges.add(Arrays.asList(sr, sc, er, ec));
    }

    public void clearHighlightRanges() {
        highlightRanges.clear();
    }
}

