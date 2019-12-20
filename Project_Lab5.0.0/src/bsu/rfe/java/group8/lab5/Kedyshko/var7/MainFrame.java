package bsu.rfe.java.group8.lab5.Kedyshko.var7;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainFrame extends JFrame
{
    // РАЗМЕРЫ ОКНА
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // ОКНО ДЛЯ ВЫБОРА ФАЙЛА И ПУНКТЫ МЕНЮ
    private  JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;

    // ОТОБРАЖИТЕЛЬ ГРАФИКА
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;


    public MainFrame()
    {
        // ОТЦЕНТРИРОВАНИЕ И ЗАДАЧА РАЗМЕРОВ ОКНА
        super("Построение графиков функций на основе заранее подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        setExtendedState(MAXIMIZED_BOTH);

        // СОЗДАНИЕ МЕНЮ И ПУНКТОВ МЕНЮ
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком")
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser==null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        fileMenu.add(openGraphicsAction);

        Action showAxisAction = new AbstractAction("Показывать оси координат")
        {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };

        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек")
        {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }

    // СЧИТЫВАНИЕ ДАННЫХ ГРАФИКА ИЗ ФАЙЛА
    protected void openGraphics(File selectedFile)
    {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            Double[][] graphicsData = new
                    Double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available()>0) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i++] = new Double[] {x, y};
            }
            if (graphicsData!=null && graphicsData.length>0)
            {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
            in.close();
        }
        catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    // ГЛАВНЫЙ МЕТОД КЛАССА
    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // СЛУШАТЕЛЬ СОБЫТИЙ
    private class GraphicsMenuListener implements MenuListener
    {
        public void menuSelected(MenuEvent e)
        {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }
        public void menuDeselected(MenuEvent e)
        {
        }
        public void menuCanceled(MenuEvent e)
        {
        }
    }
}