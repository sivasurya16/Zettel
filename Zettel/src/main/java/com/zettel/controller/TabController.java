package com.zettel.controller;

import java.net.URL;
import java.util.Arrays;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.zettel.notemanager.Note;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

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
	private static final Parser parser;
	private static final HtmlRenderer renderer;
	private final PauseTransition debounce = new PauseTransition(Duration.millis(250));

	static {
		MutableDataSet options = new MutableDataSet();
		options.set(Parser.EXTENSIONS,
				Arrays.asList(AutolinkExtension.create(), EmojiExtension.create(), StrikethroughExtension.create(),
						TaskListExtension.create(), TablesExtension.create()))
				.set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "")
				.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
				.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY);

		parser = Parser.builder(options).build();
		renderer = HtmlRenderer.builder(options).escapeHtml(true).build();
		System.out.println("✅ Flexmark parser initialized");
	}

	public void addActionListeners() {
		webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
//				splitPane.setVisible(true);
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

		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue.equals(newValue) || newValue.equals(note.textContent)) {
				return;
			}
			debounce.setOnFinished(e -> {
				setContent(newValue);
				note.edit(newValue);
			});
			debounce.playFromStart();
		});

		splitPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			double[] positions = splitPane.getDividerPositions();
			if (positions.length > 0) {
				lastDividerPosition = positions[0];
			}

			Platform.runLater(() -> splitPane.setDividerPositions(lastDividerPosition));

		});
	}

	public void initializeView(String text) {
		this.textArea.setText(text);
		this.setContent(text);
	}

	public void setTitle(String title) {
		this.title = title;
//	    note = NoteManager.availableNotes.get(title);
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getTitle() {
		return title;
	}

	@FXML
	public void initialize() {
		webEngine = webView.getEngine();
		Platform.runLater(() -> webEngine.loadContent(""));
//		splitPane.setVisible(false);
		addActionListeners();
	}

	public String generateHtml(String markdown) {
		URL base = getClass().getResource("/style");
//		MutableDataSet options = new MutableDataSet();

//		options.set(Parser.EXTENSIONS, Arrays.asList(AutolinkExtension.create(), EmojiExtension.create(),
//				StrikethroughExtension.create(), TaskListExtension.create(), TablesExtension.create()
//
//		)).set(TablesExtension.WITH_CAPTION, false).set(TablesExtension.COLUMN_SPANS, false)
//				.set(TablesExtension.MIN_HEADER_ROWS, 1).set(TablesExtension.MAX_HEADER_ROWS, 1)
//				.set(TablesExtension.APPEND_MISSING_COLUMNS, true).set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
//				.set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
//				.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
//				.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
//				.set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "");
//
//		Parser parser = Parser.builder(options).build();
//		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		Node document = parser.parse(markdown);

		String html = renderer.render(document);
		return """
				<!DOCTYPE html>
				<html>
				<head>
				    <meta charset="UTF-8">
				    <meta name="viewport" content="width=device-width, initial-scale=1">
				    <link rel="stylesheet" href="%s/preview.css">
				    <link rel="stylesheet" href="%s/highlight_js/styles/default.css">
				    <script src="%s/highlight_js/highlight.min.js"></script>
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
//		System.out.println(html);
		new Thread(() -> {
			String html = generateHtml(content);

			// Push HTML to WebView on the UI thread
			Platform.runLater(() -> {
				lastScrollPos = (Number) webEngine.executeScript("window.scrollY");
				webEngine.loadContent(html);
			});
		}).start();
	}

	public static void generateHtmlWarmup() {
		String dummy = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"file:/C:/Users/wwwsi/Documents/programs/projects/Zettel/Zettel/target/classes/style/preview.css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"file:/C:/Users/wwwsi/Documents/programs/projects/Zettel/Zettel/target/classes/style/highlight_js/styles/default.css\">\r\n"
				+ "    <script src=\"file:/C:/Users/wwwsi/Documents/programs/projects/Zettel/Zettel/target/classes/style/highlight_js/highlight.min.js\"></script>\r\n"
				+ "    <script>hljs.highlightAll();</script>\r\n" + "    <style>\r\n" + "        .markdown-body {\r\n"
				+ "            box-sizing: border-box;\r\n" + "            min-width: 200px;\r\n"
				+ "            max-width: 980px;\r\n" + "            margin: 0 auto;\r\n"
				+ "            padding: 45px;\r\n" + "        }\r\n" + "        @media (max-width: 767px) {\r\n"
				+ "            .markdown-body {\r\n" + "                padding: 15px;\r\n" + "            }\r\n"
				+ "        }\r\n" + "    </style>\r\n" + "</head>\r\n" + "<body>\r\n"
				+ "    <article class=\"markdown-body\">\r\n" + "        <h1>h1</h1>\r\n" + "<h2>h2</h2>\r\n"
				+ "<h3>h3</h3>\r\n" + "\r\n" + "    </article>\r\n" + "</body>\r\n" + "</html>";

		Node document = parser.parse(dummy);
		renderer.render(document);
	}

}
