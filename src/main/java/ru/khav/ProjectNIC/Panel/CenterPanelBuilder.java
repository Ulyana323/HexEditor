package ru.khav.ProjectNIC.Panel;

import ru.khav.ProjectNIC.Interfaces.PanelBuilder;
import ru.khav.ProjectNIC.UI_Components.TableFactory;
import ru.khav.ProjectNIC.views.MainWindow;

import javax.swing.*;
import java.awt.*;

public class CenterPanelBuilder implements PanelBuilder {
    private final TableFactory tableFactory;

    public CenterPanelBuilder(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    @Override
    public JPanel build(MainWindow mainWindow) {
        //logger.info("configCenterPanel(): PanelFactory");
        for (int i = 0; i < tableFactory.getTableData().getColumnCount(); i++) {
            tableFactory.getTableData().getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        // Прокручиваемая панель с таблицей
        JScrollPane scrollPane = new JScrollPane(tableFactory.getTableData(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollPane1 = new JScrollPane(tableFactory.getAddressTable(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Привязываем вертикальный скроллбар
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // слушаем прокрутку основной таблицы
        verticalScrollBar.addAdjustmentListener(e -> {
            scrollPane1.getVerticalScrollBar().setValue(e.getValue());
        });

        scrollPane1.setPreferredSize(new Dimension(100, scrollPane.getPreferredSize().height));

        JPanel panelWithTables = new JPanel();
        panelWithTables.setLayout(new BoxLayout(panelWithTables, BoxLayout.X_AXIS));
        panelWithTables.add(scrollPane1); //сначала адреса
        panelWithTables.add(scrollPane);  //потом данные

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(panelWithTables, BorderLayout.CENTER);
        return centerPanel;
    }
}
