/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;


/*
 *
 * @author soham
 */
class TextEditor implements ActionListener {
    private static int returnValue = 0;
    JFrame Frame = new JFrame("Visual Java Editor");
    JTextArea textarea = new JTextArea("Edit Text");
    public TextEditor(){
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(textarea);
        Frame.setSize(500, 500);
//        JToolBar ToolBar = new JToolBar("Applications");
        
        JMenuBar MenuBar = new JMenuBar();
        
        JMenu FileMenu = new JMenu("File");
        JMenu EditMenu = new JMenu("Edit");
        JMenu ViewMenu = new JMenu("View");
        JMenu RunMenu = new JMenu("Run");
        JMenu DebuggingMenu = new JMenu("Debugging");
        JMenu HelpMenu = new JMenu("Help");
        
        JMenuItem NewFile = new JMenuItem("New File");
        JMenuItem OpenFile = new JMenuItem("Open File");
        JMenuItem SaveFile = new JMenuItem("Save File");
        JMenuItem SaveAsFile = new JMenuItem("Save as File");
        JMenuItem OpenProject = new JMenuItem("Open Project");
        JMenuItem Quit = new JMenuItem("Quit");
        NewFile.addActionListener(this);
        OpenFile.addActionListener(this);
        SaveFile.addActionListener(this);
        Quit.addActionListener(this);
        
        JMenuItem Undo = new JMenuItem("Undo");
        JMenuItem Redo = new JMenuItem("Redo");
        JMenuItem Cut = new JMenuItem("Cut");
        JMenuItem Copy = new JMenuItem("Copy");
        JMenuItem Paste = new JMenuItem("Paste");
        JMenuItem Find = new JMenuItem("Find");
        JMenuItem Replace = new JMenuItem("Replace");
        JMenuItem Delete = new JMenuItem("Delete");
        
        JMenuItem ShowMenuBar = new JMenuItem("Show/Hide Menubar");
        JMenuItem FullScreen = new JMenuItem("Full Screen");
        JMenuItem Terminal = new JMenuItem("Terminal");
        JMenuItem ShowSideBar = new JMenuItem("Show Side Bar");
        
        JMenuItem RunFile = new JMenuItem("Run File");
        JMenuItem CompileFile = new JMenuItem("Compile File");
        JMenuItem RunProject = new JMenuItem("Run Project");
        JMenuItem BuildProject = new JMenuItem("Build Project");
        JMenuItem StopBuildRun = new JMenuItem("Stop Build/Run");
        
        JMenuItem DebugFile = new JMenuItem("Debug File");
        JMenuItem DebugProject = new JMenuItem("Debug Project");
        JMenuItem FinishDebuggerSession = new JMenuItem("Finish Debugging Session");
        JMenuItem Pause = new JMenuItem("Pause");
        JMenuItem Continue = new JMenuItem("Continue");
        
        JMenuItem ReportIssue = new JMenuItem("Report Issue");
        JMenuItem About = new JMenuItem("About");
        
        FileMenu.add(NewFile);
        FileMenu.addSeparator();
        FileMenu.add(OpenFile);
        FileMenu.addSeparator();
        FileMenu.add(OpenProject);
        FileMenu.addSeparator();
        FileMenu.add(SaveFile);
        FileMenu.addSeparator();
        FileMenu.add(SaveAsFile);
        FileMenu.addSeparator();
        FileMenu.add(Quit);

        EditMenu.add(Undo);
        EditMenu.addSeparator();
        EditMenu.add(Redo);
        EditMenu.addSeparator();
        EditMenu.add(Cut);
        EditMenu.addSeparator();
        EditMenu.add(Copy);
        EditMenu.addSeparator();
        EditMenu.add(Paste);
        EditMenu.addSeparator();
        EditMenu.add(Find);
        EditMenu.addSeparator();
        EditMenu.add(Replace);
        EditMenu.addSeparator();
        EditMenu.add(Delete);
        
        ViewMenu.add(FullScreen);
        ViewMenu.addSeparator();
        ViewMenu.add(ShowMenuBar);
        ViewMenu.addSeparator();
        ViewMenu.add(Terminal);
        ViewMenu.addSeparator();
        ViewMenu.add(ShowSideBar);
        
        RunMenu.add(RunFile);
        RunMenu.addSeparator();
        RunMenu.add(CompileFile);
        RunMenu.addSeparator();
        RunMenu.add(RunProject);
        RunMenu.addSeparator();
        RunMenu.add(BuildProject);
        RunMenu.addSeparator();
        RunMenu.add(StopBuildRun);
        
        DebuggingMenu.add(DebugFile);
        DebuggingMenu.addSeparator();
        DebuggingMenu.add(DebugProject);
        DebuggingMenu.addSeparator();
        DebuggingMenu.add(FinishDebuggerSession);
        DebuggingMenu.addSeparator();
        DebuggingMenu.add(Pause);
        DebuggingMenu.addSeparator();
        DebuggingMenu.add(Continue);
        
        HelpMenu.add(ReportIssue);
        HelpMenu.addSeparator();
        HelpMenu.add(About);
        
        MenuBar.add(FileMenu);
        MenuBar.add(EditMenu);
        MenuBar.add(ViewMenu);
        MenuBar.add(RunMenu);
        MenuBar.add(DebuggingMenu);
        MenuBar.add(HelpMenu);
        
        Frame.setJMenuBar(MenuBar);
        Frame.setLayout(new BorderLayout());
        //Frame.getContentPane().add(ToolBar, BorderLayout.PAGE_START);
        Frame.setVisible(true);
        
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent actionevent) {
        String ingest = null;
        JFileChooser JFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        JFC.setDialogTitle("Choose Destination");
        JFC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        String getCommand = actionevent.getActionCommand();
        if(getCommand.equals("Open File")){
            returnValue = JFC.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                File file = new File(JFC.getSelectedFile().getAbsolutePath());
                try {
                    FileReader readfile = new FileReader(file);
                    Scanner Scan = new Scanner(readfile);
                    while(Scan.hasNextLine()){
                        String Line = Scan.nextLine() + "\n";
                        ingest = ingest + Line;
                    } textarea.setText(ingest);
                }catch(FileNotFoundException ex){
                    ex.printStackTrace();
                }
            }else if(getCommand.equals("Save File")){
                returnValue = JFC.showSaveDialog(null);
                try{
                    File file = new File(JFC.getSelectedFile().getAbsolutePath());
                    try (FileWriter OUT = new FileWriter(file)) {
                        OUT.write(textarea.getText());
                    }
                } catch (FileNotFoundException ex) {
                   Component F = null;
                   JOptionPane.showMessageDialog(F, "File not found.");
                } catch (IOException ex) {
                    Component F = null;
                    JOptionPane.showMessageDialog(F, "Error.");
                }
            }else if (getCommand.equals("New File")){
                textarea.setText("");
            }else if(getCommand.equals("Quit")){
                System.exit(0);
            }
        }
    }

}
public class Editor {
    public static void main(String[] args){
        TextEditor Run = new TextEditor();
    }
}
