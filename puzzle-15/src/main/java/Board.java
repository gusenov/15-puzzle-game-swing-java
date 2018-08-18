import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

/** Игровое поле. */
public class Board extends JPanel {

    /** Интервал между ячейками на игровом поле. */
    private static final int BOARD_GAP = 8;

    /** Количество строк. */
    private int rows;

    /** Количество столбцов. */
    private int cols;

    /** Массив фишек задаётся связным списком. */
    private List<Chip> chips = new LinkedList<Chip>();

    /** Список в который записываются последние перемещенные фишки. */
    private List<Chip> lastMovedChips = new LinkedList<Chip>();

    /** Ссылка на панель для информации (о результатах игры и т. п.). */
    private Status status;

    /** Ссылка на панель управления. */
    private Control control;

    /**
     * Конструктор.
     *
     * @param rows  количество строк.
     * @param cols  количество столбцов.
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        createGUI();  // создать графический интерфейс игрового поля.
    }

    /** Метод для создания графического интерфейса игрового поля. */
    private void createGUI() {
        setLayout(new GridLayout(rows, cols, BOARD_GAP, BOARD_GAP));  // установка сетки в качестве раскладки.

        int cells = getCellCount();  // количество ячеек на игровом поле.
        for (int i = 1; i <= cells; ++i) {  // цикл по всем ячейкам игрового поля:
            Cell cell = new Cell(i - 1);  // создание новой ячейки.
            add(cell);  // добавление ячейки на игровое поле.

            // Создание фишек:
            if (i < cells) {
                Chip chip = new Chip(i);  // новая фишка.

                // Код выполняющийся при нажатии на фишку:
                chip.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        Chip chip = (Chip)actionEvent.getSource();  // получение фишки из произошедшего события.
                        clickChip(chip, true);
                    }
                });

                chips.add(chip);  // добавление фишки в связный список фишек.
            }
        }

        fill();
    }

    /** Метод для очистки всех ячеек игрового поля от фишек. */
    private void cleanCells() {
        // Цикл по всем дочерним компонентам, которые на самом деле являются фишками:
        for (Component component : getComponents()) {
            Cell cell = (Cell)component;  // очередная ячейка.
            if (!cell.isEmpty()) {  // если ячейка не пустая, то:
                cell.removeAll();  // удалить всё её содержимое.

                // Перерисовать ячейку:
                cell.revalidate();
                cell.repaint();
            }
        }
    }

    /** Заполнение поля фишками в случайном порядке. */
    public void fill() {

        cleanCells();  // предварительно очищаем игровое поле.
        lastMovedChips.clear();  // очищаем историю перемещения фишек.

        Component[] cells = (Component[])getComponents();  // массив всех ячеек игрового поля.

        int cellIdx = 0;  // индекс ячейки.
        int elemIdx = 0;  // индекс фишки.

        // Процесс зацикливается до тех пор,
        // пока в списке не останется 0 элементов:
        while (chips.size() != 0) {
            elemIdx = Utils.randInt(0, chips.size() - 1);  // случайным образом выбирается индекс фишки.
            Chip chip = chips.get(elemIdx);  // фишка из связного списка.

            Cell cell = (Cell)cells[cellIdx];  // очередная ячейка.
            cell.removeAll();  // удаляем содержимое ячейки.

            // Перерисовать ячейку:
            cell.revalidate();
            cell.repaint();

            cell.add(chip);  // фишка размещается на игровом поле (последовательно) в очередной ячейке.
            ++cellIdx;

            // Одновременно фишка удаляется из списка, чтобы не участвовать в следующем размещении:
            chips.remove(elemIdx);
        }

        // Снова добавляем удаленные из связного списка фишки обратно в связный список:
        for (Component component : cells) {
            Cell cell = (Cell)component;
            if (!cell.isEmpty()) {  // если ячейка не пустая, то:
                Chip chip = cell.getChip();
                chips.add(chip);
            }
        }

    }

    /**
     * Получить количество ячеек на игровом поле.
     *
     * @return  количество ячеек.
     */
    public int getCellCount() {
        return rows * cols;
    }

    /**
     * Получить ячейку по строке и столбцу.
     *
     * @param row  номер строки в которой располагается ячейка.
     * @param col  номер столбца в котором располагается ячейка.
     * @return  ячейка.
     */
    public Cell getCell(int row, int col) {
        int cellIdx = Utils.convertIndex2DTo1D(row, col, rows);  // преобразование двумерного индекса в одномерный.
        Cell cell = (Cell)getComponent(cellIdx);
        return cell;
    }

    /**
     * Получить ячейку по индексу.
     *
     * @param index  индекс ячейки.
     * @return  ячейка.
     */
    public Cell getCell(int index) {
        if (index >= 0 && index < getComponentCount()) {
            Component[] cells = (Component[])getComponents();
            return (Cell)cells[index];
        }
        return null;
    }

    /**
     * Метод для проверки валидности координат ячейки.
     *
     * @param row  номер строки в которой располагается ячейка.
     * @param col  номер столбца в котором располагается ячейка.
     * @return  true - если такая ячейка есть на игровом поле; false - иначе.
     */
    public boolean isValidCoor(int row, int col) {
        return row >= 0
                && col >= 0
                && row < rows
                && col < cols;
    }

    /**
     * Обмен фишками для двух ячеек.
     *
     * @param coor1  кординаты первой ячейки.
     * @param coor2  координаты второй ячейки.
     */
    public void swapCells(int[] coor1, int[] coor2) {
        Cell cell1 = getCell(coor1[0], coor1[1]);  // получение первой ячейки по её координатам.
        Chip chip1 = null;
        if (!cell1.isEmpty()) {  // если первая ячейка не пустая, то:
            chip1 = cell1.getChip();
            cell1.removeAll();

            // Перерисовать первую ячейку:
            cell1.revalidate();
            cell1.repaint();
        }

        Cell cell2 = getCell(coor2[0], coor2[1]);  // получение второй ячейки по её координатам.
        Chip chip2 = null;
        if (!cell2.isEmpty()) {  // если вторая ячейка не пустая, то:
            chip2 = cell2.getChip();
            cell2.removeAll();

            // Перерисовать вторую ячейку:
            cell2.revalidate();
            cell2.repaint();
        }

        if (chip2 != null) {
            cell1.add(chip2);

            // Перерисовать первую ячейку:
            cell1.revalidate();
            cell1.repaint();
        }

        if (chip1 != null) {
            cell2.add(chip1);

            // Перерисовать вторую ячейку:
            cell2.revalidate();
            cell2.repaint();
        }
    }

    /**
     * Перемещение фишки при клике на неё.
     *
     * @param chip  фишка.
     * @param remember  true - перемещение записывается в историю и его можно отменить; false - не записывается.
     */
    public void clickChip(Chip chip, boolean remember) {
        if (chip == null) {
            return;
        }

        // Получаем ячейку в которой расположена фишка и координаты этой ячейки:
        Cell cell = (Cell)chip.getParent();
        int[] coor = Utils.convertIndex1DTo2D(cell.getPosition(), rows, cols);
        int row = coor[0];
        int col = coor[1];

        boolean moved = false;  // флаг послужащий индикатором того, что фишка была перемещена.

        // Движение влево:
        if (isValidCoor(row - 1, col)  // проверка, что есть ячейка слева.
                && getCell(row - 1, col).isEmpty()) {
            swapCells(coor, new int[] { row - 1, col });
            moved = true;

        // Движение вправо:
        } else if (isValidCoor(row + 1, col)  // проверка, что есть ячейка справа.
                && getCell(row + 1, col).isEmpty()) {
            swapCells(coor, new int[] { row + 1, col });
            moved = true;

        // Движение вверх:
        } else if (isValidCoor(row, col - 1)  // проверка, что есть ячейка вверху.
                && getCell(row, col - 1).isEmpty()) {
            swapCells(coor, new int[] { row, col - 1 });
            moved = true;

        // Движение вниз:
        } else if (isValidCoor(row , col + 1)  // проверка, что есть ячейка внизу.
                && getCell(row, col + 1).isEmpty()) {
            swapCells(coor, new int[] { row, col + 1 });
            moved = true;
        }

        if (moved) {  // если фишка была перемещена:
            if (remember) {
                lastMovedChips.add(chip);  // добавление фишки в историю последних перемещённых фишек.
                control.enableUndoMove(true);  // включение возможности отмены хода.
                status.incMoves();  // увеличение счётчика ходов.
            }
            chip.requestFocus();  // установить фокус ввода на перемещённую фишку.
        }

        // Проверка текущего состояния игрового поля:

        if (badLuck()) {  // если фишки 14 и 15 оказались поменяны местами, то выиграть не возможно:
            status.setResult(Result.BAD_LUCK);  // не повезло.

        } else if (victory()) {  // если фишки выстроены по номерам, начиная с верхнего левого угла, то:
            status.setResult(Result.VICTORY);  // победа.

        } else {  // в противном случае результат игры пока не определён:
            status.setResult(Result.NONE);
        }
    }

    /** Отменить последний ход. */
    public void undoMove() {
        if (lastMovedChips.isEmpty()) {  // если ходов нет, то:
            return;  // выход из метода.
        }

        int chipIdx = lastMovedChips.size() - 1;  // индекс последней сдвинутой фишки.
        Chip chip = lastMovedChips.get(chipIdx);  // последняя сдвинутая фишка.
        lastMovedChips.remove(chipIdx);  // удаление из истории последнего хода.

        clickChip(chip, false);  // возврат фишки на прежнее место.
        status.decMoves();  // уменьшение значения счётчика ходов.

        if (lastMovedChips.isEmpty()) {  // если история стала пустой, то:
            control.enableUndoMove(false);  // выключить кнопку отмены хода.
        }
    }

    /**
     * Проверка на то, оказались ли фишки 14 и 15 поменяны местами.
     * Если так, то то выиграть не возможно.
     *
     * @return  true - фишки 14 и 15 поменяны местами; false - иначе.
     */
    public boolean badLuck() {
        int cellCount = getCellCount();  // количество всех фишек на игровом поле.

        // Проверка, что фишки от 1 до 13 упорядочены согласно их номерам:
        for (int i = 0; i < cellCount - 3; i++) {
            Cell cell = getCell(i);  // очередная фишка.
            if (i + 1 != cell.getChipNumber()) {
                return false;
            }
        }

        // Проверка фишек 14 и 15:
        if (cellCount - 1 != getCell(cellCount - 3).getChipNumber()
                || cellCount - 2 != getCell(cellCount - 2).getChipNumber()) {
            return false;
        }

        return true;
    }

    /**
     * Проверка на то выстроены ли фишки по номерам, начиная с верхнего левого угла.
     *
     * @return  true - выстроены; false - иначе.
     */
    public boolean victory() {
        int cellCount = getCellCount();  // количество всех фишек на игровом поле.

        // Проверка, что фишки от 1 до 15 упорядочены согласно их номерам:
        for (int i = 0; i < cellCount - 1; i++) {
            Cell cell = getCell(i);
            if (i + 1 != cell.getChipNumber()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Включить или выключить фишки.
     *
     * @param isEnabled  true - фишки можно будет перемещать; false - фишки будут неподвижны.
     */
    public void enableChips(boolean isEnabled) {
        Component[] cells = (Component[])getComponents();  // все ячейки игрового поля.
        for (Component component : cells) {  // обход всех ячеек в цикле.
            Cell cell = (Cell)component;  // очередная ячейка.
            if (!cell.isEmpty()) {  // если ячейка не пустая, то:
                Chip chip = cell.getChip();  // фишка находящаяся в ячейке.
                chip.setEnabled(isEnabled);
                chip.setFocusable(isEnabled);
            }
        }
    }


    /**
     * Получить ссылку на панель информации.
     *
     * @return  ссылка на панель информации.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Установить ссылку на панель информации.
     *
     * @param status  ссылка на панель информации.
     */
    public void setStatus(Status status) {
        this.status = status;
    }


    /**
     * Получить ссылку на панель управления.
     *
     * @return  ссылка на панель управления.
     */
    public Control getControl() {
        return control;
    }

    /**
     * Установить ссылку на панель управления.
     *
     * @param control  ссылка на панель управления.
     */
    public void setControl(Control control) {
        this.control = control;
    }


    /** Отладочный метод для заполнения игрового поля выигрышным порядком фишек. */
    public void fillSeq() {
        cleanCells();  // предварительно очищаем игровое поле.
        lastMovedChips.clear();  // очищаем историю перемещения фишек.

        Component[] cells = (Component[])getComponents();   // массив всех ячеек игрового поля.
        int cellCount = getCellCount();  // количество ячеек на игровом поле.
        for (int i = 0; i < cellCount - 1; ++i) {
            Chip chip = chips.get(i);
            Cell cell = (Cell)cells[i];
            cell.add(chip);  // добавить фишку в ячейку.

            // Перерисовать ячейку:
            cell.revalidate();
            cell.repaint();
        }
    }

    /** Отладочный метод для заполнения игрового поля безвыигрышным порядком фишек. */
    public void fillBadLuck() {
        cleanCells();  // предварительно очищаем игровое поле.
        lastMovedChips.clear();  // очищаем историю перемещения фишек.

        Chip chip = null;  // фишка.
        Cell cell = null;  // ячейка.

        Component[] cells = (Component[])getComponents();   // массив всех ячеек игрового поля.
        int cellCount = getCellCount();  // количество ячеек на игровом поле.
        for (int i = 0; i < cellCount - 1; ++i) {

            if (i < cellCount - 3) {
                chip = chips.get(i);
                cell = (Cell)cells[i];
            } else if (i == cellCount - 3) {
                chip = chips.get(cellCount - 2);
                cell = (Cell)cells[cellCount - 3];
            } else if (i == cellCount - 2) {
                chip = chips.get(cellCount - 3);
                cell = (Cell)cells[cellCount - 2];
            }

            cell.add(chip);  // добавить фишку в ячейку.

            // Перерисовать ячейку:
            cell.revalidate();
            cell.repaint();
        }  // i
    }
}
