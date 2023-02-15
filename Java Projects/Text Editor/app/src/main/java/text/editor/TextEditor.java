package text.editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class TextEditor extends JFrame {

    private enum Direction {
        NEXT,
        PREVIOUS,
    }

    private CycleIterator<Match> iteratorTextMatch;
    private Match matchObject;
    final private JCheckBox checkBoxRegEx = new JCheckBox();


    ////////////////////////////////////////
    public static String path = System.getProperty("user.dir");
    ////////////////////////////////////
    private JFileChooser jFileChooser;
    /////////////////////////////////
    private JMenuBar jMenuBar;
    private JMenu fileMenu;
    private JMenu search;
    private JMenuItem MenuLoad;
    private JMenuItem MenuSave;
    private JMenuItem MenuExit;
    private JMenuItem MenuSearch;
    private JMenuItem MenuPrevious;
    private JMenuItem MenuNext;
    private JMenuItem MenuReg;
    ////////////////////////////
    private JPanel panel;
    final private JTextArea textArea = new JTextArea();
    private JScrollPane scrollableTextArea;
    /////////////////////////////////
    private JPanel upperPanel;

    private JButton saveButton;
    private JButton openButton;
    private JTextField searchField;
    final private JTextField searchFieldText = new JTextField();
    private JButton startSearchButton;
    private JButton previousMatchButton;
    private JButton nextMatchButton;
    private JCheckBox regexCheckBox;

    ///////////////////////////////////////////////


    public TextEditor() {
        SwingUtilities.invokeLater(this::createUI); //I don't know what is :: /// new runnable (Dispatch thread)
    }
    private void createUI() {
        setTitle("Text Editor");
        setMinimumSize(new Dimension(800, 800));
        getContentPane().setBackground(Color.darkGray);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        //fileMenu chooser init
        jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setName("FileChooser");
        add(jFileChooser);

        initMenu();
        initUI();

        setVisible(true);
    }
    private void initUI() {

        //main panel
        panel = new JPanel();
        panel.setBackground(Color.blue);
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        //upper panel
        upperPanel = new JPanel();
        upperPanel.setBackground(Color.black);
        upperPanel.setLayout(new FlowLayout());
        panel.add(upperPanel, BorderLayout.NORTH);

        //buttons
        saveButton = new JButton("Save");
        saveButton.setName("SaveButton");

        openButton = new JButton("Open");
        openButton.setName("OpenButton");

        openButton.addActionListener(event -> openFile());
        saveButton.addActionListener(event ->{
            getPathToSave();
            saveFile();
        });

        upperPanel.add(saveButton);
        upperPanel.add(openButton);

        //search field
        searchField = new JTextField(30);
        searchField.setName("SearchField");
        searchField.setBackground(Color.darkGray);
        searchField.setForeground(Color.green);
        upperPanel.add(searchField);

        //buttons
        startSearchButton = new JButton("find");
        startSearchButton.setName("StartSearchButton");
        previousMatchButton = new JButton("<-");
        previousMatchButton.setName("PreviousMatchButton");
        nextMatchButton = new JButton("->");
        nextMatchButton.setName("NextMatchButton");
        upperPanel.add(startSearchButton);
        upperPanel.add(previousMatchButton);
        upperPanel.add(nextMatchButton);

        startSearchButton.addActionListener(event -> {
            clearMatchesInfo();
            if (!textArea.getText().isEmpty()) {
                ///Method possibleToSelect will not work for this way
                createMatch();
            }});
        previousMatchButton.addActionListener(event -> {
            if (isPossibleToSearch()) {
                highlightMatch(Direction.PREVIOUS);
            }
        });
        nextMatchButton.addActionListener(event -> {
            if (isPossibleToSearch()) {
                highlightMatch(Direction.NEXT);
            }
        });


        //checkbox
        regexCheckBox = new JCheckBox("use regex");
        regexCheckBox.setName("UseRegExCheckbox");
        upperPanel.add(regexCheckBox);

        //text editor
        textArea.setName("TextArea");
       textArea.setBackground(Color.black);
       textArea.setForeground(Color.green);
        panel.add(textArea, BorderLayout.CENTER);


        //scroll bar
        scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setName("ScrollPane");
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setBackground(Color.black);
        panel.add(scrollableTextArea);

    }
    private void initMenu() {
        //menu bar init
        jMenuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        search = new JMenu("Search");
        search.setName("MenuSearch");

        //menu items
        MenuLoad = new JMenuItem("Open");
        MenuLoad.setName("MenuOpen");
        MenuSave = new JMenuItem("Save");
        MenuSave.setName("MenuSave");
        MenuExit = new JMenuItem("Exit");
        MenuExit.setName("MenuExit");
        fileMenu.add(MenuLoad);
        fileMenu.add(MenuSave);
        fileMenu.add(MenuExit);
        jMenuBar.add(fileMenu);
        setJMenuBar(jMenuBar);

        MenuLoad.addActionListener(event -> openFile());
        MenuSave.addActionListener(event ->{
            getPathToSave();
            saveFile();
        });
        MenuExit.addActionListener(event -> exit());

        //menu items
        MenuSearch = new JMenuItem("Start search");
        MenuSearch.setName("MenuStartSearch");
        MenuPrevious = new JMenuItem("Previous match");
        MenuPrevious.setName("MenuPreviousMatch");
        MenuNext = new JMenuItem("Menu next match");
        MenuNext.setName("MenuNextMatch");
        MenuReg = new JMenuItem("Use regex");
        MenuReg.setName("MenuUseRegExp");
        search.add(MenuSearch);
        search.add(MenuPrevious);
        search.add(MenuNext);
        search.add(MenuReg);
        jMenuBar.add(search);
        setJMenuBar(jMenuBar);

        MenuSearch.addActionListener(event -> {
            clearMatchesInfo();
            if (!searchField.getText().isEmpty()) {
                ///Method possibleToSelect will not work for this way
                createMatch();
            }});
        MenuPrevious.addActionListener(event -> {
        if (isPossibleToSearch()) {
                highlightMatch(Direction.PREVIOUS);
            }});
        MenuNext.addActionListener(event -> {
            if (isPossibleToSearch()) {
                highlightMatch(Direction.NEXT);
            }
        });
        MenuReg.addActionListener(event -> regexCheckBox.setSelected(!regexCheckBox.isSelected()));

    }

    //open
    private void openFile() {
        textArea.setText("");
        jFileChooser.setDialogTitle("Choose open file");
        int returnValue = jFileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File file = jFileChooser.getSelectedFile();
            final String path = file.getAbsolutePath();
            try {
                textArea.setText(Files.readString(Paths.get(path)));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("uncorrected open file");
            }
        }
    }

    //save
    private void getPathToSave() {
        jFileChooser.setDialogTitle("Choose save path");
        jFileChooser.showSaveDialog(null);
    }

    private void saveFile() {
        try {
            final FileWriter fileWriter = new FileWriter(jFileChooser.getSelectedFile());
            fileWriter.write(textArea.getText());
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("uncorrected save path");
        }

    }


    private void clearMatchesInfo() {
        matchObject = null;
        iteratorTextMatch = null;
    }

    private String stringEscaping() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < searchField.getText().length(); i++) {
            String x = String.valueOf(searchField.getText().charAt(i));
            if (!x.matches("[\\h]") && x.matches("[\\W]")) {
                buffer.append('\\');
            }
            buffer.append(x);
        }
        return buffer.toString();
    }

    private void createMatch() {
        if (!regexCheckBox.isSelected()) {
            new BackgroundWork(stringEscaping()).execute();
        } else {
            new BackgroundWork(searchField.getText()).execute();
        }
    }

    private void highlightMatch(Direction dir) {
        if ("NEXT".equals(dir.name())) {
            matchObject = iteratorTextMatch.next();
        }
        if ("PREVIOUS".equals(dir.name())) {

            matchObject = iteratorTextMatch.previous();
        }

        if (isGroupNotChanged()) {
            textArea.setCaretPosition(matchObject.getEnd());
            textArea.select(matchObject.getFirst(), matchObject.getEnd());
            textArea.grabFocus();
        }
    }


    private boolean isGroupNotChanged() {
        String str = textArea.getText().substring(
                matchObject.getFirst(), matchObject.getEnd());
        return matchObject.getGroup().equals(str);
    }

    private boolean isPossibleToSearch() {
        return iteratorTextMatch != null && !textArea.getText().isEmpty() && !searchField.getText().isEmpty();
    }



private class BackgroundWork extends SwingWorker<Void, Object> {
    private String searchFieldText;

    BackgroundWork(String searchFieldText) {
        this.searchFieldText = searchFieldText;
    }

    @Override
    public Void doInBackground() {

        final Matcher matcher = Pattern.compile(searchFieldText).matcher(textArea.getText());
        final ArrayList<Match> listOfMatch = new ArrayList<>();

        while (matcher.find()) {
            listOfMatch.add(new Match(matcher.start(), matcher.end(), matcher.group()));
        }

        iteratorTextMatch = new CycleIterator<>(listOfMatch);

        highlightMatch(TextEditor.Direction.NEXT);///first call always 0;
        return null;
    }
}

 private static class Match {
    final private int first;
    final private int end;
    final private String group;

    Match(final int first, final int end, final String group) {
        this.first = first;
        this.end = end;
        this.group = group;
    }

    int getFirst() {
        return first;
    }

    int getEnd() {
        return end;
    }

    String getGroup() {
        return group;
    }
}

private static class CycleIterator<T> implements ListIterator<T> {
    private final ArrayList<T> list;
    private int pos = -1;

    CycleIterator(ArrayList<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return pos != list.size() - 1;
    }

    @Override
    public T next() {
        if (hasNext()) {
            pos++;
        } else {
            pos = 0;
        }
        return list.get(pos);

    }

    @Override
    public boolean hasPrevious() {
        return pos != 0;
    }

    @Override
    public T previous() {
        if (hasPrevious()) {
            pos--;
        } else {
            pos = list.size() - 1;
        }
        return list.get(pos);
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return 0;
    }

    @Override
    public void remove() {
    }

    @Override
    public void set(Object o) {
    }

    @Override
    public void add(T o) {
        list.add(o);
    }
}


    public static void main(final String[] args) {
        new TextEditor();
    }
    //exit
    private void exit() {
        System.exit(0);
    }
}
