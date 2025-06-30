package com.zettel.gui;

import java.net.URL;
import java.util.Arrays;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.resizable.image.ResizableImageExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zettel.notemanager.Note;
import com.zettel.notemanager.NoteManager;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class TabController {

	@FXML
	protected TextArea textArea;
	@FXML
	private WebView webView;
	@FXML
	private SplitPane splitPane;

	private double lastDividerPosition = 0.5;
	private Number lastScrollPos = 0;
	private WebEngine webEngine;
	private String title;
	private Note note;
	
	public void addActionListeners() {
		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			setContent(newValue);
			
            note.edit(newValue);
	        

		});

		webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				webEngine.executeScript("window.scrollTo(0, " + lastScrollPos + ");");
				// Add a click listener to every anchor tag
				org.w3c.dom.Document doc = webView.getEngine().getDocument();
				org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("a");

				for (int i = 0; i < nodeList.getLength(); i++) {
					org.w3c.dom.events.EventTarget target = (org.w3c.dom.events.EventTarget) nodeList.item(i);
					target.addEventListener("click", evt -> {
						evt.preventDefault(); // Prevent WebView from handling it
						String href = ((org.w3c.dom.Element) evt.getCurrentTarget()).getAttribute("href");
						if (href != null && !href.isEmpty()) {
							try {
								java.awt.Desktop.getDesktop().browse(new java.net.URI(href));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, false);
				}
			}
		});
	}
	
	public void setTitle(String title) {
	    this.title = title;
	    note = NoteManager.availableNotes.get(title);
	}

	public String getTitle() {
	    return title;
	}
	@FXML
	public void initialize() {
		webEngine = webView.getEngine();
		splitPane.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustDivider());
		
	}

	private void adjustDivider() {
		double[] positions = splitPane.getDividerPositions();
		if (positions.length > 0) {
			lastDividerPosition = positions[0];
		}

		Platform.runLater(() -> splitPane.setDividerPositions(lastDividerPosition));
	}

	public String generateHtml(String markdown) {
		MutableDataSet options = new MutableDataSet();
		URL base = getClass().getResource("/preview_format");

		options.set(Parser.EXTENSIONS, Arrays.asList(AutolinkExtension.create(), EmojiExtension.create(),
				StrikethroughExtension.create(), TaskListExtension.create(), TablesExtension.create()

		)).set(TablesExtension.WITH_CAPTION, false).set(TablesExtension.COLUMN_SPANS, false)
				.set(TablesExtension.MIN_HEADER_ROWS, 1).set(TablesExtension.MAX_HEADER_ROWS, 1)
				.set(TablesExtension.APPEND_MISSING_COLUMNS, true).set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
				.set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
				.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
				.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
				.set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "");

		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		Node document = parser.parse(markdown);

		String html = renderer.render(document);
		return """
				<!DOCTYPE html>
				<html>
				<head>
				    <meta charset="UTF-8">
				    <meta name="viewport" content="width=device-width, initial-scale=1">
				    <link rel="stylesheet" href="%s/preview.css">
				    <link rel="stylesheet" href="%s/styles/default.css">
				    <script src="%s/highlight.min.js"></script>
				    <script>hljs.highlightAll();</script>
				    <style>
				        .markdown-body {
				            box-sizing: border-box;
				            min-width: 200px;
				            max-width: 980px;
				            margin: 0 auto;
				            padding: 45px;
				        }
				        @media (max-width: 767px) {
				            .markdown-body {
				                padding: 15px;
				            }
				        }
				    </style>
				</head>
				<body>
				    <article class="markdown-body">
				        %s
				    </article>
				</body>
				</html>
				""".formatted(base, base, base, html);
	}

	public void setContent(String content) {
		lastScrollPos = (Number) webEngine.executeScript("window.scrollY");
		String html = generateHtml(content);
		webEngine.loadContent(html);
	}

//	public void setContent(String title) {
//		textArea.setText("Content of " + title);
//		webEngine.loadContent("<h1>" + title + "</h1><p>This is a note</p>");
//	}
}
