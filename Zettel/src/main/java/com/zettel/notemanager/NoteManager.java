package com.zettel.notemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class NoteManager {
	public static URI BASEURI;
	public static TreeItem<File> rootNode;
	public static Map<String,Note> availableNotes;

	public NoteManager() {
		try {
			BASEURI = new URI("file:///C:/Users/wwwsi/Documents/notes");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		rootNode = createNode(new File(BASEURI));
	}

	public Map<String,Note> getAvailableNotes() {
		return availableNotes;
	}
	
	public String getNoteContent(File file) throws FileNotFoundException {
		FileReader fr = new FileReader(file);
		StringBuilder result = new StringBuilder();
		int ch;
		try {
			while ((ch = fr.read()) != -1) {
				result.append((char)ch);
			}
			
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
		
	}

//	public void getNotes(URI CURURI,TreeItem<Note> baseStruture) {
//		File directory = new File(CURURI);
//
//		if (!directory.exists() || !directory.isDirectory()) {
//			System.err.println("Invalid directory: " + CURURI);
//			return;
//		}
//		
//		
//		
//		File[] files = directory.listFiles();
//		if (files != null) {
//			for (File file : files) {
//				if (file.isDirectory()) {
////					TreeItem<> newStructure = new TreeItem<>();
////					createNode()
//				}
//				else {
//					String title = file.getName();
//					URI location = file.toURI();
//					StringBuilder textContent = new StringBuilder();
//
//					try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//						String line;
//						while ((line = br.readLine()) != null) {
//							textContent.append(line).append("\n");
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//					Note n = new Note(title, textContent.toString().trim(), location);
//					TreeItem<Note> node = new TreeItem<>(n);
////					baseStruture.getChildren().add(node);
////					availableNotes.put(title,n);
//				}
////				availableNotes.put(title,n);
//				
//			}
//		}
//
//	}

	
    private TreeItem<File> createNode(final File file) {
        return new TreeItem<>(file) {

            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    isLeaf = file.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> treeItem) {
                File f = treeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                        for (File childFile : files) {
                            children.add(createNode(childFile)); // Recursive call
                        }
                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }
        };
    }
    
    public void updateRootNode() {
    	rootNode = createNode(new File(BASEURI));
    	rootNode.setExpanded(true);
    }
    
    public static void changeBaseDirectory(File newBaseDir) {
        if (newBaseDir != null && newBaseDir.isDirectory()) {
            BASEURI = newBaseDir.toURI();
        }
    }
	
}
