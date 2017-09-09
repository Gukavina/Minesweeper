import java.awt.*;//отвечает за отображение окна
import java.awt.event.*;//отвечает за событие, в наше случае щелчёк мышки
import javax.imageio.ImageIO;
import javax.swing.*;//рисует различные объектыы в окне
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
//В принципе программа состоит из трёх основных частей. Конструктора, класса Cell и метода инициализации поля.
//класс игры наследуется от класса JFrame
class Minesweeper extends JFrame {
    // финал - переменная не будет изменятся в процессе программы.
    final String TITLE_OF_PROGRAM = "Mines";//заголовок
    final String SIGN_OF_FLAG = "f";//
    final int BLOCK_SIZE = 30; // 30*30 размер 1 блока
    static int fieldSizeX;
    static int fieldSizeY;
    final int FIELD_DX = 6;
    final int FIELD_DY = 28 + 17;
    static final int START_LOCATION = 0;//стартовая координата окна
    final int MOUSE_BUTTON_LEFT = 1; //константа, которая возвращается при нажатии на левую кнопку
    final int MOUSE_BUTTON_RIGHT = 3;//на правую соответственно
    static int numberOfMines;//количество мин
    final int[] COLOR_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};//массив цветов. содержит цвета мин и цифр
    //селл - класс, который написан чуть ниже. Двумерный массив объектов клеточек(ячеек)
    Cell[][] field;
    //поможет случайным образом расставить мины
    Random random = new Random();
    //колиество открытых нами ячеек
    int countOpenedCells;
    //переменные выйгрыша, проигрыша
    boolean youWon, bangMine;
    static boolean modeSquareOrHexagon = true;
    //координаты взрыва
    int bangX, bangY;

    //главный метод.
    public static void main(String[] args) {
        JDialog dialogStart = new JDialog();
        dialogStart.setBounds(START_LOCATION,START_LOCATION,320,360);
        JPanel panelDialogStart = new JPanel();
        panelDialogStart.setSize(320 ,360);
        JButton start = new JButton();
        start.setText("Начать");
        start.setBounds(50,195, 100, 30);
        JTextField sizeX = new JTextField(20);
        JTextField sizeY = new JTextField(20);
        JTextField mines = new JTextField(20);
        JLabel labelX = new JLabel();
        labelX.setText("Введите количество ячеек по горизонтали:");
        JLabel labelY = new JLabel();
        labelY.setText("Введите количество ячеек по вертикали:");
        JLabel labelMines = new JLabel();
        labelMines.setText("Введите количество мин:");
        JLabel lvl = new JLabel();
        lvl.setText("Или выберите стандартный уровень сложности:");
        JCheckBox squareMode = new JCheckBox();
        JButton lvl1 = new JButton();
        lvl1.setText("Новичок");
        lvl1.setSize(100,30);
        lvl1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldSizeX = 9;
                fieldSizeY = 9;
                numberOfMines = 10;
            }
        });
        JButton lvl2 = new JButton();
        lvl2.setText("Любитель");
        lvl2.setSize(100,30);
        lvl2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldSizeX = 16;
                fieldSizeY = 16;
                numberOfMines = 40;
            }
        });
        JButton lvl3 = new JButton();
        lvl3.setText("Профессионал");
        lvl3.setSize(100,30);
        lvl3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldSizeX = 30;
                fieldSizeY = 16;
                numberOfMines = 99;
            }
        });
        JLabel labelMode = new JLabel();
        labelMode.setText("Выберите тип ячейки:                          ");
        squareMode.setText("квадрат            ");
        JCheckBox hexagonMode = new JCheckBox();
        hexagonMode.setText("шестиугольник");

        squareMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modeSquareOrHexagon = true;
            }
        });
        hexagonMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modeSquareOrHexagon = false;
            }
        });


        sizeX.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fieldSizeX = Integer.parseInt(sizeX.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fieldSizeX = Integer.parseInt(sizeX.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fieldSizeX = Integer.parseInt(sizeX.getText());
            }
        });
        sizeY.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fieldSizeY = Integer.parseInt(sizeY.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fieldSizeY = Integer.parseInt(sizeY.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fieldSizeY = Integer.parseInt(sizeY.getText());
            }
        });
        mines.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                numberOfMines = Integer.parseInt(mines.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                numberOfMines = Integer.parseInt(mines.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                numberOfMines = Integer.parseInt(mines.getText());
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogStart.dispose();
                new Minesweeper();
            }
        });
        dialogStart.add(panelDialogStart);
        panelDialogStart.add(labelX);
        panelDialogStart.add(sizeX);
        panelDialogStart.add(labelY);
        panelDialogStart.add(sizeY);
        panelDialogStart.add(labelMines);
        panelDialogStart.add(mines);
        panelDialogStart.add(lvl);
        panelDialogStart.add(lvl1);
        panelDialogStart.add(lvl2);
        panelDialogStart.add(lvl3);
        panelDialogStart.add(labelMode);
        panelDialogStart.add(squareMode);
        panelDialogStart.add(hexagonMode);
        panelDialogStart.add(start);
        dialogStart.setVisible(true);
       //создание объекта на основании класса. Наша программа является классом и во время старта создается объект данного класса.
    }

    //конструктор, запускается один раз при создании класса. Специфический метод для иницилизации объекта класса.
    Minesweeper() {
        if (numberOfMines == 0)  numberOfMines = 10;
        if (fieldSizeX == 0)  fieldSizeX = 9;
        if (fieldSizeX > 45) fieldSizeX = 45;
        if (fieldSizeY == 0)  fieldSizeY = 9;
        if (fieldSizeY > 23) fieldSizeY = 23;

        field =  new Cell[fieldSizeX][fieldSizeY];
        //вызываем методы от унаследованного класса
        setTitle(TITLE_OF_PROGRAM);//определяем заголовок программы
        setDefaultCloseOperation(EXIT_ON_CLOSE);//закрытие программы с помощью крестика в правом верхнем углу
        setBounds(START_LOCATION, START_LOCATION, fieldSizeX * BLOCK_SIZE + FIELD_DX, fieldSizeY * BLOCK_SIZE + FIELD_DY);//начальное значение окна
        setResizable(false);//возможность масштабирование окна

        final TimerLabel timeLabel = new TimerLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //определить канву, класс который  у нас создан.
        final Canvas canvas = new Canvas();
        canvas.setBackground(Color.white);//белый цвет подложки

        //определить прослушиватель нажатия на мышь. Что будет делать "канва" при нажатии на мышь.
        canvas.addMouseListener(new MouseAdapter() {
            //описываем mouseadapter. Оверрайд - переопределяем, переписывает метод.
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);//вызываем метод родительского класса
                //получаем абсолютные координаты клика. Делим на размер блока и получаем относительные координыты
                int x = e.getX()/BLOCK_SIZE;
                int y = e.getY()/BLOCK_SIZE;
                //основная логика сапёра сосредоточена в обработчике нажатия на ту или иную кнопку мыши
                //что делается при щелчке левой кнопки мыши
                if (!bangMine && !youWon) {
                    if (e.getButton() == MOUSE_BUTTON_LEFT) // left button mouse
                        if (field[x][y].isNotOpen()) {
                            openCells(x, y);
                            youWon = countOpenedCells == fieldSizeX*fieldSizeY - numberOfMines; // winning check
                            if (bangMine) {
                                bangX = x;
                                bangY = y;
                            }
                        }

                    //при нажатии правой кнопки
                    if (e.getButton() == MOUSE_BUTTON_RIGHT) field[x][y].inverseFlag(); // right button mouse
                    //факт окончания игры
                    if (bangMine || youWon){
                        timeLabel.stopTimer();
                        JDialog dialog = new JDialog();
                        dialog.setResizable(false);
                        dialog.setTitle("Конец игры");
                        dialog.setSize(280,140);
                        dialog.setLocation(START_LOCATION + 3, START_LOCATION + 20);
                        JButton newGame = new JButton("Новая игра");
                        newGame.setSize(140,70);
                        newGame.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String arg[] = new String[0];
                                dialog.dispose();
                                dispose();
                                main(arg);
                            }
                        });
                        JButton close = new JButton("Завершить");
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        close.setSize(140,70);


                        ImPanel panel = new ImPanel();
                        dialog.setContentPane(panel);
                        panel.add(newGame);
                        panel.add(close);
                        dialog.setVisible(true);

                    }
                    //перерисовываем канву.
                    canvas.repaint();
                }
            }
        });


        add(BorderLayout.CENTER, canvas);
        add(BorderLayout.SOUTH, timeLabel);
        setVisible(true);
        //инициализация, делает поле видимым
        initField();
    }

        //метод, который обеспечивает открытие пустых ячеек
    //рекурсивный метод. Вызывает сам себя. Условия выхода
    void openCells(int x, int y) {
        if (x < 0 || x > fieldSizeX - 1 || y < 0 || y > fieldSizeY - 1) return; //1. координаты выходят за границы поля
        if (!field[x][y].isNotOpen()) return; //2.если ячейка открыта
        field[x][y].open();
        if (field[x][y].getCountBomb() > 0 || bangMine) return; //3. если количество бомб больше нуля
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) openCells(x + dx, y + dy);
    }

    //перед началом игры необходимо проинициализировать поле
    //состоит из трёх частей.
    void initField() {
        //координаты и подсчет мин
        int x, y, countMines = 0;
        //1. Создаёт каждую клеточку, заполняет поле. Игровое поле - двумерный массив объектов
        for (x = 0; x < fieldSizeX; x++)
            for (y = 0; y < fieldSizeY; y++)
                field[x][y] = new Cell();

        //2. Рандомно расставляет мины
        while (countMines < numberOfMines) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            } while (field[x][y].isMined());
            field[x][y].mine();
            countMines++;
        }

        //3. Часть, подсчитывающая мины вокруг.
        for (x = 0; x < fieldSizeX; x++)
            for (y = 0; y < fieldSizeY; y++)
                if (!field[x][y].isMined()) {
                    int count = 0;
                    for (int dx = -1; dx < 2; dx++)
                        for (int dy = -1; dy < 2; dy++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > fieldSizeX - 1 || nY > fieldSizeY - 1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nX][nY].isMined()) ? 1 : 0;
                        }
                    field[x][y].setCountBomb(count);
                }
    }

    class ImPanel extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;
            try {
                if (bangMine)
                    im = ImageIO.read(new File("src\\проигрыш.png"));
                else im =ImageIO.read(new File("src\\выйгрыш.png"));
            } catch (IOException e) {
            }
            g.drawImage(im, 0, 0, null);
        }
    }

    //класс ячейки
    class Cell { // playing field cell
        //количество бомб внизу
        private int countBombNear;
        //открыта ли ячейка, если ли мина, есть ли флаг
        private boolean isOpen, isMine, isFlag;
        //открытие ячейки
        void open() {
            isOpen = true;
            //глобальная переменная проигрыша - подрыв
            bangMine = isMine;
            //увеличиваем сетчик открытых ячеек
            if (!isMine) countOpenedCells++;
        }

        //позволяет минировать ячейку
        void mine() { isMine = true; }

        //метод, который устанавливает количество бомб. сеттер
        void setCountBomb(int count) { countBombNear = count; }

        //Геттер. Возвращает колиество бомб
        int getCountBomb() { return countBombNear; }

        //проверяется открыта ли ячейка
        boolean isNotOpen() { return !isOpen; }

        //заминирована ли ячейка
        boolean isMined() { return isMine; }

        //инверсирование флага
        void inverseFlag() { isFlag = !isFlag; }

        //нарисовать бомбу
        void paintBomb(Graphics g, int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x*BLOCK_SIZE + 7, y*BLOCK_SIZE + 10, 18, 10);
            g.fillRect(x*BLOCK_SIZE + 11, y*BLOCK_SIZE + 6, 10, 18);
            g.fillRect(x*BLOCK_SIZE + 9, y*BLOCK_SIZE + 8, 14, 14);
            g.setColor(Color.white);
            g.fillRect(x*BLOCK_SIZE + 11, y*BLOCK_SIZE + 10, 4, 4);
        }

        //метод рисование строки(отображать цифру, или же флаг)
        void paintString(Graphics g, String str, int x, int y, Color color) {
            g.setColor(color);
            g.setFont(new Font("", Font.BOLD, BLOCK_SIZE));
            g.drawString(str, x*BLOCK_SIZE + 8, y*BLOCK_SIZE + 26);
        }

        void paint(Graphics g, int x, int y) {
            //рисует серенький прямоугольничек и расчерчивает поле на квадратики
            g.setColor(Color.lightGray);
            g.drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

            //если ячейка не открыта.
            if (!isOpen) {
                //если открыта и там мина - рисуется  мина, если другой кнопкой. то флаг.
                if ((bangMine || youWon) && isMine) paintBomb(g, x, y, Color.black);
                else {
                    g.setColor(Color.lightGray);
                    g.fill3DRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, true);
                    if (isFlag) paintString(g, SIGN_OF_FLAG, x, y, Color.red);
                }
            } else
                //тут про количество бомб вокруг
                if (isMine) paintBomb(g, x, y, bangMine? Color.red : Color.black);
                else
                if (countBombNear > 0)
                    paintString(g, Integer.toString(countBombNear), x, y, new Color(COLOR_OF_NUMBERS[countBombNear - 1]));
        }
    }

    //таймер, наследуемый класс
    class TimerLabel extends JLabel { // label with stopwatch
        Timer timer = new Timer();

        //конструктор
        TimerLabel() { timer.scheduleAtFixedRate(timerTask, 0, 1000); } // TimerTask task, long delay, long period

        TimerTask timerTask = new TimerTask() {
            volatile int time;
            Runnable refresher = new Runnable() {
                public void run() {
                    TimerLabel.this.setText(String.format("%02d:%02d", time / 60, time % 60));
                }
            };
            public void run() {
                time++;
                SwingUtilities.invokeLater(refresher);
            }
        };

        void stopTimer() { timer.cancel(); }
    }
    //класс псевдоканвы. Канва для рисования
    class Canvas extends JPanel { // my canvas for painting
        @Override
        public void paint(Graphics g) {
            //вызываем родительский метод отрисовки
            super.paint(g);
            //благодаря этим циклам мы обращаемся к объекту в массиве field и вызываем метод отрисовки этого объекта
             for (int x = 0; x < fieldSizeX; x++)
                for (int y = 0; y < fieldSizeY; y++) field[x][y].paint(g, x, y);
        }
    }
}

