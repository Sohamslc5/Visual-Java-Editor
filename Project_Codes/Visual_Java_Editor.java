/* Object Oriented Methodologies Mini Project: Visual Java Editor */
/* Group Number: 38 */
/* Group Members:
 *              IIB2021043 - Soham Chauhan
 *              IIB2021044 - HimaBindu Jadhav
 *              IIB2021007 - Thakur Aman
 */
import java.awt.*;
import java.io.*;
import java.util.Date;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/************************************/
class FileOperation {
    Visual_Java_Editor JavaEditor;
    boolean saved;
    boolean newFileFlag;
    static String fileName;
    static String tempFile;
    String applicationTitle = "Visual_Java_Editor";
    File fileRef;
    JFileChooser chooser;

    //if file is saved, return saved
    boolean isSave() {
        return saved;
    }
    //make boolean saved = saved
    void setSave(boolean saved) {
        this.saved = saved;
    }
    //print filename
    public String getFileName() {
        return new String(fileName);
    }
    //set the name of file
    void setFileName(String fileName) {
        this.fileName = new String(fileName);
    }

    //Constructor for FileOperation
    FileOperation(Visual_Java_Editor JavaEditor) {

        this.JavaEditor = JavaEditor;
        saved = true;
        newFileFlag = true;
        fileName = "Untitled";
        fileRef = new File(fileName);
        this.JavaEditor.Frame.setTitle(fileName + " - " + applicationTitle);

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

    }
    //return true if file can be saved else or return false
    boolean saveFile(File temp) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(temp);
            fileWriter.write(JavaEditor.TextArea.getText());
        } catch (IOException ioe) {
            updateStatus(temp, false);
            return false;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException excp) {
            }
        }
        updateStatus(temp, true);
        tempFile = temp.getName();
        return true;
    }

    //after saving change the boolean of newFile to false, cause now it's not a new file anymore
    boolean saveThisFile() {

        if (!newFileFlag) {
            return saveFile(fileRef);
        }

        return saveAsFile();
    }

    //save as function, if save as action is provoked
    boolean saveAsFile() {
        File temp = null;
        chooser.setDialogTitle("Save As...");
        chooser.setApproveButtonText("Save Now");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
        chooser.setApproveButtonToolTipText("Click me to save!");

        do {
            if (chooser.showSaveDialog(this.JavaEditor.Frame) != JFileChooser.APPROVE_OPTION)
                return false;
            temp = chooser.getSelectedFile();
            if (!temp.exists())
                break;
            if (JOptionPane.showConfirmDialog(
                    this.JavaEditor.Frame, "<html>" + temp.getPath() + " already exists.<br>Do you want to replace it?<html>","Save As", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                break;
        } while (true);

        return saveFile(temp);
    }

    //if user wants to open any file
    boolean openFile(File temp) {
        FileInputStream fin = null;
        BufferedReader din = null;

        try {
            fin = new FileInputStream(temp);
            din = new BufferedReader(new InputStreamReader(fin));
            String str = " ";
            while (str != null) {
                str = din.readLine();
                if (str == null)
                    break;
                this.JavaEditor.TextArea.append(str + "\n");
            }

        } catch (IOException ioe) {
            updateStatus(temp, false);
            return false;
        } finally {
            try {
                din.close();
                fin.close();
            } catch (IOException excp) {
            }
        }
        updateStatus(temp, true);
        this.JavaEditor.TextArea.setCaretPosition(0);
        return true;
    }

    //implementation of the method that open file
    void openFile() {
        if (!confirmSave()) {
            return;
        }
        chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
        chooser.setDialogTitle("Open File...");
        chooser.setApproveButtonText("Open this");
        chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        chooser.setApproveButtonToolTipText("Click me to open the selected file.!");

        File temp = null;
        do {
            if (chooser.showOpenDialog(this.JavaEditor.Frame) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            temp = chooser.getSelectedFile();

            if (temp.exists()) {
                break;
            }

            JOptionPane.showMessageDialog(this.JavaEditor.Frame,"<html>" + temp.getName() + "<br>file not found.<br>" + "Please verify the correct file name was given.<html>","Open", JOptionPane.INFORMATION_MESSAGE);

        } while (true);

        this.JavaEditor.TextArea.setText("");

        if (!openFile(temp)) {
            fileName = "Untitled";
            saved = true;
            this.JavaEditor.Frame.setTitle(fileName + " - " + applicationTitle);
        }
        if (!temp.canWrite()) {
            newFileFlag = true;
        }

    }

    //implementation of the method that saves the file whenever savefile is invoked
    void updateStatus(File temp, boolean saved) {
        if (saved) {
            this.saved = true;
            fileName = new String(temp.getName());
            if (!temp.canWrite()) {
                fileName += "(Read only)";
                newFileFlag = true;
            }
            fileRef = temp;
            JavaEditor.Frame.setTitle(fileName + " - " + applicationTitle);
            JavaEditor.statusBar.setText("File : " + temp.getPath() + " saved/opened successfully.");
            newFileFlag = false;
        } else {
            JavaEditor.statusBar.setText("Failed to save/open : " + temp.getPath());
        }
    }

    //implementation of the method that asks for confirmation of saving the current file or not
    boolean confirmSave() {
        String strMsg = "<html>The text in the " + fileName + " file has been changed.<br>" + "Do you want to save the changes?<html>";
        if (!saved) {
            int x = JOptionPane.showConfirmDialog(this.JavaEditor.Frame, strMsg, applicationTitle,JOptionPane.YES_NO_CANCEL_OPTION);

            if (x == JOptionPane.CANCEL_OPTION) {
                return false;
            }
            if (x == JOptionPane.YES_OPTION && !saveAsFile()) {
                return false;
            }
        }
        return true;
    }
    //implementation of the method that creates a new file whenever asked
    void newFile() {
        if (!confirmSave()) {
            return;
        }

        this.JavaEditor.TextArea.setText("");
        fileName = new String("Untitled");
        fileRef = new File(fileName);
        saved = true;
        newFileFlag = true;
        this.JavaEditor.Frame.setTitle(fileName + " - " + applicationTitle);
    }
    //////////////////////////////////////
}
/************************************/
public class Visual_Java_Editor implements ActionListener, MenuConstants {

    JFrame Frame;
    JTextArea TextArea, lines;
    JLabel statusBar;
    JScrollPane ScrollPane;
    private String fileName = "Untitled";

    private boolean saved = true;
    String applicationName = "Visual Java Editor";

    String searchString, replaceString;

    int lastSearchIndex, select_start = -1, startIndex = 0;
    FileOperation fileHandler;
    JTextField TextField = new JTextField();
    JColorChooser bcolorChooser = null;
    JColorChooser fcolorChooser = null;
    JDialog backgroundDialog = null;
    JDialog foregroundDialog = null;
    JMenuItem cutItem, copyItem, deleteItem, findItem, findNextItem, undoItem, replaceItem, gotoItem, selectAllItem, redoItem;
    Font font;
    static int count = 0;
// In the constructor
    /****************************/
    public Visual_Java_Editor() {
        Frame = new JFrame(fileName + " - " + applicationName);
        lines = new JTextArea();
        JPanel panel = new JPanel();

        TextArea = new JTextArea(30, 60);
        TextArea.setTabSize(2);
//        font = TextArea.getFont();
        font = new Font(Font.SANS_SERIF,Font.BOLD, 20);
        TextArea.setFont(font);
        statusBar = new JLabel("||       Ln 1, Col 1  ", JLabel.RIGHT);
        Frame.add(new JScrollPane(TextArea), BorderLayout.CENTER);
        Frame.add(statusBar, BorderLayout.SOUTH);
        Frame.add(new JLabel("  "), BorderLayout.EAST);
        Frame.add(new JLabel("  "), BorderLayout.WEST);
        createMenuBar(Frame);
        Frame.pack();
        Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        fileHandler = new FileOperation(this);

        //implementation for Undo and Redo operation
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);

        UndoManager undoManager = new UndoManager();

        Document document = TextArea.getDocument();
        document.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        // Add ActionListeners
        undoItem.addActionListener((ActionEvent e) -> {
            try {
                count++;
                undoManager.undo();
            } catch (CannotUndoException cue) {}
        });
        redoItem.addActionListener((ActionEvent e) -> {
            try {
                count--;
                undoManager.redo();
            } catch (CannotRedoException cre) {}
        });

        // Map undo action
        TextArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
        TextArea.getActionMap().put("undoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                } catch (CannotUndoException cue) {}
            }
        });
        //Map redo action
        TextArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
        TextArea.getActionMap().put("redoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                } catch (CannotRedoException cre) {}
            }
        });

        //adding auto bracket closing
        TextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == '{'){
                    TextArea.insert("}", TextArea.getCaretPosition());
                    TextArea.setCaretPosition(TextArea.getCaretPosition() - 2);
                }
                if(e.getKeyChar() == '('){
                    TextArea.insert(")", TextArea.getCaretPosition());
                    TextArea.setCaretPosition(TextArea.getCaretPosition() - 2);
                }
                if(e.getKeyChar() == '['){
                    TextArea.insert("]", TextArea.getCaretPosition());
                    TextArea.setCaretPosition(TextArea.getCaretPosition() - 2);
                }
                if(e.getKeyChar() == '"'){
                    TextArea.insert("\"", TextArea.getCaretPosition());
                    TextArea.setCaretPosition(TextArea.getCaretPosition() - 2);
                }
                if(e.getKeyChar() == '\''){
                    TextArea.insert("\'", TextArea.getCaretPosition());
                    TextArea.setCaretPosition(TextArea.getCaretPosition() - 2);
                }
            }
        });

        //implementation of the modification of line number and column number on the StatusBar
        TextArea.addCaretListener(
                new CaretListener() {
                    public void caretUpdate(CaretEvent e) {
                        int lineNumber = 0, column = 0, pos = 0;

                        try {
                            pos = TextArea.getCaretPosition();
                            lineNumber = TextArea.getLineOfOffset(pos);
                            column = pos - TextArea.getLineStartOffset(lineNumber);
                        } catch (Exception excp) {
                        }
                        if (TextArea.getText().length() == 0) {
                            lineNumber = 0;
                            column = 0;
                        }
                        statusBar.setText("||       Ln " + (lineNumber + 1) + ", Col " + (column + 1));
                    }
                });
        //Adding document listener
        DocumentListener myListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }
            public void removeUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }
            public void insertUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }
        };
        TextArea.getDocument().addDocumentListener(myListener);
        //This is the implementation of the window that prompts when we edit a file and we don't save it then, there comes a prompt reminding about the file
        WindowListener frameClose = new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (fileHandler.confirmSave())
                    System.exit(0);
            }
        };
        Frame.addWindowListener(frameClose);
    }

    public void find() {
        select_start = TextArea.getText().indexOf(TextField.getText().toLowerCase());
        if(select_start == -1)
        {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Could not find \"" + TextField.getText() + "\"!");
            return;
        }
        if(select_start == TextArea.getText().lastIndexOf(TextField.getText().toLowerCase()))
        {
            startIndex = 0;
        }
        int select_end = select_start + TextField.getText().length();
        TextArea.select(select_start, select_end);
    }
    public void findNext() {
        String selection = TextArea.getSelectedText();
        try
        {
            selection.equals("");
        }
        catch(NullPointerException e)
        {
            selection = TextField.getText();
            try
            {
                selection.equals("");
            }
            catch(NullPointerException e2)
            {
                selection = JOptionPane.showInputDialog("Find:");
                TextField.setText(selection);
            }
        }
        try
        {
            int select_start = TextArea.getText() .indexOf(selection , startIndex);
            int select_end = select_start+selection.length();
            TextArea.select(select_start, select_end);
            startIndex = select_end+1;

            if(select_start == TextArea.getText().lastIndexOf(selection.toLowerCase()))
            {
                startIndex = 0;
            }
        }
        catch(NullPointerException e)
        {}
    }
    //implementation of the method which gets the cursor's location to the starting of any desired line number
    void goTo() {
        int lineNumber = 0;
        try {
            lineNumber = TextArea.getLineOfOffset(TextArea.getCaretPosition()) + 1;
            String tempStr = JOptionPane.showInputDialog(Frame, "Enter Line Number:", "" + lineNumber);
            if (tempStr == null) {
                return;
            }
            lineNumber = Integer.parseInt(tempStr);
            TextArea.setCaretPosition(TextArea.getLineStartOffset(lineNumber - 1));
        } catch (Exception e) {
        }
    }

    //implementing action listeners for every event
    public void actionPerformed(ActionEvent ev) {
        String e = ev.getActionCommand(); //getting in string what command/event occured
        //actionListener for making a new file
        if (e.equals(fileNew)) {
            fileHandler.newFile();
        }
        //actionListener for opening a file
        else if (e.equals(fileOpen)) {
            fileHandler.openFile();
        }
        //actionListener for saving a newly edited file
        else if (e.equals(fileSave)) {
            fileHandler.saveThisFile();
        }
        //actionListener for saving as a newly edited file
        else if (e.equals(fileSaveAs)) {
            fileHandler.saveAsFile();
        }
        //actionListener for exiting the whole window
        else if (e.equals(fileExit)) {
            if (fileHandler.confirmSave()) {
                System.exit(0);
            }
        }
        //actionListener for printing a file
        else if (e.equals(filePrint)) {
            JOptionPane.showMessageDialog(Visual_Java_Editor.this.Frame, "Get ur printer repaired first! It seems u dont have one!","Bad Printer", JOptionPane.INFORMATION_MESSAGE);
        }
        //actionListener for cut method in TextArea of a file
        else if (e.equals(editCut)) {
            TextArea.cut();
        }
        //actionListener for copy method in TextArea of a file
        else if (e.equals(editCopy)) {
            TextArea.copy();
        }
        //actionListener for paste (the cutted/copied string) method in TextArea of a file
        else if (e.equals(editPaste)) {
            TextArea.paste();
        }
        //actionListener for delete method for selected text in TextArea of a file
        else if (e.equals(editDelete)) {
            TextArea.replaceSelection("");
        }
        //actionListener for help menuitem
        else if(e.equals(helpHelpTopic)){
            String Help = "Contact Us on our Mails:\niib2021043@iiita.ac.in\niib2021044@iiita.ac.in\niib2021007@iiita.ac.in";
            JOptionPane.showMessageDialog(Visual_Java_Editor.this.Frame, Help,  aboutText,JOptionPane.INFORMATION_MESSAGE);
        }
        //actionListener for finding method for any desired word in TextArea of a file
        else if (e.equals(editFind)) {
            find();
            if (Visual_Java_Editor.this.TextArea.getText().length() == 0)
                return; // text box have no text
            else{
                final String inputValue = JOptionPane.showInputDialog("Find What?");
                final int l1 = TextArea.getText().indexOf(inputValue);
                final int l2 = inputValue.length();
                if (l1 == -1) {
                    JOptionPane.showMessageDialog(null, "Search Value Not Found");
                } else {
                    TextArea.select(l1, l2+l1);
                }
            }
        }
        ////actionListener for finding next occurences method for any desired word in TextArea of a file
        else if (e.equals(editFindNext)) {
            findNext();
        }
        //actionListener for replacing {all the occurences of a desired word to some other desired word at place only} method for any desired word in TextArea of a file
        else if (e.equals(editReplace)) {
            if (Visual_Java_Editor.this.TextArea.getText().length() == 0) {
                return; // text box have no text
            }
            else{
                final String inputValue = JOptionPane.showInputDialog("Find What?");
                final String inputValue2 = JOptionPane.showInputDialog("Replace it?");
                final int l1 = TextArea.getText().indexOf(inputValue);
                final int l2 = inputValue.length();
                if (l1 == -1) {
                    JOptionPane.showMessageDialog(null, "Search Value Not Found");
                } else {
                    TextArea.setText(TextArea.getText().replace(inputValue, inputValue2));
                }
            }
        }
        //actionListener for goTo method
        else if (e.equals(editGoTo)) {
            if (Visual_Java_Editor.this.TextArea.getText().length() == 0) {
                return; // text box have no text
            }
            goTo();
        }
        //actionListener for selecting all the Text of a TextArea
        else if (e.equals(editSelectAll)) {
            TextArea.selectAll();
        }
        //actionListener for printing time and date on any desired line in TextArea of a file
        else if (e.equals(editTimeDate)) {
            TextArea.insert(new Date().toString(), TextArea.getSelectionStart());
        }
        //actionListener for wordwrap method which wrappes up word
        else if (e.equals(formatWordWrap)) {
            JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
            TextArea.setLineWrap(temp.isSelected());
        }
        //actionListener for increasFont method that increases the font of the text in TextArea of a file
        else if (e.equals(formatFont)) {
            Font font = TextArea.getFont();
            float size = font.getSize() + 2.0f;
            TextArea.setFont(font.deriveFont(size));
        }
        //actionListener for changing the font color in TextArea of a file
        else if (e.equals(formatForeground)) {
            showForegroundColorDialog();
        }
        //actionListener for changing the background color in TextArea of a file
        else if (e.equals(formatBackground)) {
            showBackgroundColorDialog();
        }
        //actionListener for Setting StatusBar visibility in TextArea of a file
        else if (e.equals(viewStatusBar)) {
            JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
            statusBar.setVisible(temp.isSelected());
        }
        //actionListener for run method to compile and run any java file
        else if(e.equals(runFile)) {
            String filekapath = fileHandler.fileRef.getParent();
            String im = "";
            for(int i = 2; i < filekapath.length(); i++){
                im = im + filekapath.charAt(i);
            }
            String command = "cd " + im;
            String name = fileHandler.fileRef.getName();
            String command2 = "javac " + name;
            String temp = name;
            name = "";
            for (int i = 0; i < temp.length() - 5; i++)
            {
                name = name + temp.charAt(i);
            }
            name = "java " + name;
            try {
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd.. && cd.. && cd..&& cd.. && cd.. && cd.. && cd.. && cd.. && cd.. && cd.. && " + command + " && " + command2 + " && " + name + "\"");
            } catch (IOException vve) {
                throw new RuntimeException(vve);
            }
        }
        //actionListener for compile method to compile any java file
        else if(e.equals(compileFile)){
            String filekapath = fileHandler.fileRef.getParent();
            String im = "";
            for(int i = 2; i < filekapath.length(); i++){
                im = im + filekapath.charAt(i);
            }
            String command = "cd " + im;
            String name = fileHandler.fileRef.getName();
            String command2 = "javac " + name;
            String temp = name;
            name = "";
            for (int i = 0; i < temp.length() - 5; i++)
            {
                name = name + temp.charAt(i);
            }
            name = "java " + name;
            try {
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd.. && cd.. && cd..&& cd.. && cd.. && cd.. && cd.. && cd.. && cd.. && cd.. && " + command + " && " + command2 + "\"");
            } catch (IOException vve) {
                throw new RuntimeException(vve);
            }
        }
        //actionListener for decreaseFont method that decreases the font of the text in TextArea of a file
        else if(e.equals(decreaseFont)){
            Font font = TextArea.getFont();
            float size = font.getSize() - 2.0f;
            TextArea.setFont(font.deriveFont(size));
        }
        //actionListener for stop Building method to immediately stop the execution of the java file
        else if(e.equals(stopBuild)){
            try {
                Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        //actionListener for help Jmenuitem in TextArea of a file
        else if (e.equals(helpAboutNotepad)) {
            String aboutus = "Creators:\nSoham Chauhan - IIB2021043\nHimaBindu Jadhav - IIB2021044\nAman Thakur - IIB2021007";
            JOptionPane.showMessageDialog(Visual_Java_Editor.this.Frame, aboutus,  aboutText,JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusBar.setText("This " + e + " command is yet to be implemented");
        }
    }// action Performed
    ////////////////////////////////////
    //implementation of the method that shows the background color dialog box which is used to change the background color
    void showBackgroundColorDialog() {
        if (bcolorChooser == null) {
            bcolorChooser = new JColorChooser();
        }
        if (backgroundDialog == null)
            backgroundDialog = JColorChooser.createDialog(Visual_Java_Editor.this.Frame,formatBackground,false,bcolorChooser,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent evvv) {
                            Visual_Java_Editor.this.TextArea.setBackground(bcolorChooser.getColor());
                        }
                    }, null);

        backgroundDialog.setVisible(true);
    }

    //implementation of the method that shows the font color dialog box which is used to change the font color
    void showForegroundColorDialog() {
        if (fcolorChooser == null) {
            fcolorChooser = new JColorChooser();
        }
        if (foregroundDialog == null)
            foregroundDialog = JColorChooser.createDialog(Visual_Java_Editor.this.Frame,formatForeground,false,fcolorChooser,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent evvv) {
                            Visual_Java_Editor.this.TextArea.setForeground(fcolorChooser.getColor());
                        }
                    }, null);
        foregroundDialog.setVisible(true);
    }

    //implementation of the method that creates menuitem with name, key , action listener as input
    JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        toMenu.add(temp);

        return temp;
    }

    //implementation of the method that creates menuitem with name, key ,preview of shortcut key, action listener as input
    JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        temp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
        toMenu.add(temp);

        return temp;
    }

    //implementation of the method that creates a checkbox menuitem with name, key , action listener as input
    JCheckBoxMenuItem createCheckBoxMenuItem(String s,int key, JMenu toMenu, ActionListener al) {
        JCheckBoxMenuItem temp = new JCheckBoxMenuItem(s);
        temp.setMnemonic(key);
        temp.addActionListener(al);
        temp.setSelected(false);
        toMenu.add(temp);

        return temp;
    }

    //implementation of the method that creates menu with name, key as input
    JMenu createMenu(String s, int key, JMenuBar toMenuBar) {
        JMenu temp = new JMenu(s);
        temp.setMnemonic(key);
        toMenuBar.add(temp);
        return temp;
    }

    /*********************************/
    //implementation of the method that creates MenuBar with JFrame Frame as input
    void createMenuBar(JFrame Frame) {
        JMenuBar mb = new JMenuBar();
        JMenuItem temp;

        JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, mb);
        JMenu editMenu = createMenu(editText, KeyEvent.VK_E, mb);
        JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, mb);
        JMenu viewMenu = createMenu(viewText, KeyEvent.VK_V, mb);
        JMenu Run_Debug = createMenu(run_compile, KeyEvent.VK_R, mb);
        JMenu helpMenu = createMenu(helpText, KeyEvent.VK_H, mb);

        createMenuItem(runFile, KeyEvent.VK_R,Run_Debug,KeyEvent.VK_T, this);
        createMenuItem(compileFile, KeyEvent.VK_B, Run_Debug, KeyEvent.VK_B, this);
        Run_Debug.addSeparator();
        createMenuItem(stopBuild, KeyEvent.VK_W, Run_Debug, this);

        createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
        createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
        createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
        createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, this);
        fileMenu.addSeparator();
        temp = createMenuItem(filePageSetup, KeyEvent.VK_U, fileMenu, this);
        temp.setEnabled(false);
        createMenuItem(filePrint, KeyEvent.VK_P, fileMenu, KeyEvent.VK_P, this);
        fileMenu.addSeparator();
        createMenuItem(fileExit, KeyEvent.VK_X, fileMenu, this);

        undoItem = createMenuItem(editUndo, KeyEvent.VK_U, editMenu, KeyEvent.VK_Z, this);
        redoItem = createMenuItem(editRedo, KeyEvent.VK_Y, editMenu, KeyEvent.VK_Y, this);
        editMenu.addSeparator();
        cutItem = createMenuItem(editCut, KeyEvent.VK_T, editMenu, KeyEvent.VK_X, this);
        copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
        createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V, this);
        deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        editMenu.addSeparator();
        findItem = createMenuItem(editFind, KeyEvent.VK_F, editMenu, KeyEvent.VK_F, this);
        findNextItem = createMenuItem(editFindNext, KeyEvent.VK_N, editMenu, this);
        findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        replaceItem = createMenuItem(editReplace, KeyEvent.VK_R, editMenu, KeyEvent.VK_H, this);
        gotoItem = createMenuItem(editGoTo, KeyEvent.VK_G, editMenu, KeyEvent.VK_G, this);
        editMenu.addSeparator();
        selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_A, this);
        createMenuItem(editTimeDate, KeyEvent.VK_D, editMenu, this).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        createCheckBoxMenuItem(formatWordWrap, KeyEvent.VK_W, formatMenu, this);
        createMenuItem(formatFont, KeyEvent.VK_F, formatMenu, KeyEvent.VK_M ,this);
        createMenuItem(decreaseFont, KeyEvent.VK_F, formatMenu, KeyEvent.VK_L, this);
        formatMenu.addSeparator();
        createMenuItem(formatForeground, KeyEvent.VK_T, formatMenu, this);
        createMenuItem(formatBackground, KeyEvent.VK_P, formatMenu, this);
        createCheckBoxMenuItem(viewStatusBar, KeyEvent.VK_S, viewMenu, this).setSelected(true);
        temp = createMenuItem(helpHelpTopic, KeyEvent.VK_H, helpMenu, this);
        temp.setEnabled(true);
        helpMenu.addSeparator();
        createMenuItem(helpAboutNotepad, KeyEvent.VK_A, helpMenu, this);

        MenuListener editMenuListener = new MenuListener() {
            public void menuSelected(MenuEvent evvvv) {
                if (Visual_Java_Editor.this.TextArea.getText().length() == 0) {
                    findItem.setEnabled(false);
                    findNextItem.setEnabled(false);
                    replaceItem.setEnabled(false);
                    selectAllItem.setEnabled(false);
                    gotoItem.setEnabled(false);
                } else {
                    findItem.setEnabled(true);
                    findNextItem.setEnabled(true);
                    replaceItem.setEnabled(true);
                    selectAllItem.setEnabled(true);
                    gotoItem.setEnabled(true);
                }
                if (Visual_Java_Editor.this.TextArea.getSelectionStart() == TextArea.getSelectionEnd()) {
                    undoItem.setEnabled(true);
                    cutItem.setEnabled(false);
                    copyItem.setEnabled(false);
                    deleteItem.setEnabled(false);
                } else {
                    undoItem.setEnabled(true);
                    cutItem.setEnabled(true);
                    copyItem.setEnabled(true);
                    deleteItem.setEnabled(true);
                }
                if(count > 0){
                    redoItem.setEnabled(true);
                }else{
                    redoItem.setEnabled(false);
                }
            }

            public void menuDeselected(MenuEvent evvvv) {
            }

            public void menuCanceled(MenuEvent evvvv) {
            }
        };
        editMenu.addMenuListener(editMenuListener);
        mb.setBorderPainted(true);
//        mb.setBackground(Color.LIGHT_GRAY);
        Frame.setJMenuBar(mb);
        TextArea.setBackground(Color.LIGHT_GRAY);
        TextArea.setFont(Font.getFont(Font.MONOSPACED, TextArea.getFont()));

        Frame.setUndecorated(false);
        Frame.setBackground(Color.LIGHT_GRAY);
        Frame.setVisible(true);
    }

    /************* Constructor **************/
    ////////////////////////////////////
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc)
        {
            System.err.println("Error loading L&F: " + exc);
        }
//        Visual_Java_Editor k = new Visual_Java_Editor();
        new Visual_Java_Editor();
    }
}

/**************************************/
interface MenuConstants {
    final String stopBuild = "Stop Build";
    final String runFile = "Run File";
    final String compileFile = "Compile File";
    final String fileText = "File";
    final String editText = "Edit";
    final String formatText = "Format";
    final String viewText = "View";
    final String helpText = "Help";
    final String run_compile = "Run and Compile";
    final String fileNew = "New";
    final String fileOpen = "Open...";
    final String fileSave = "Save";
    final String fileSaveAs = "Save As...";
    final String filePageSetup = "Page Setup...";
    final String filePrint = "Print";
    final String fileExit = "Exit";
    final String editUndo = "Undo";
    final String editRedo = "Redo";
    final String editCut = "Cut";
    final String editCopy = "Copy";
    final String editPaste = "Paste";
    final String editDelete = "Delete";
    final String editFind = "Find...";
    final String editFindNext = "Find Next";
    final String editReplace = "Replace";
    final String editGoTo = "Go To...";
    final String editSelectAll = "Select All";
    final String editTimeDate = "Time/Date";
    final String formatWordWrap = "Word Wrap";
    final String formatFont = "Increase Font";
    final String decreaseFont = "Decrease Font";
    final String formatForeground = "Set Text color...";
    final String formatBackground = "Set Pad color...";
    final String viewStatusBar = "Status Bar";
    final String helpHelpTopic = "Help Topic";
    final String helpAboutNotepad = "About Visual Java Editor";
    final String aboutText = "Our Visual Java Editor...";
}
