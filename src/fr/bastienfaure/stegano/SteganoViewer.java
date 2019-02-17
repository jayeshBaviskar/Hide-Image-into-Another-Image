package fr.bastienfaure.stegano;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;


public class SteganoViewer {

	private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
	private JFrame frmStegano;
	private BufferedImage currentImage;
	private ImagePanel imageSourcePanel;
	private ImagePanel imageHidePanel;
	private ImagePanel imageResultPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SteganoViewer window = new SteganoViewer();
					Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					window.frmStegano.setLocation(d.width/2 - window.frmStegano.getWidth()/2, d.height/2 - window.frmStegano.getHeight()/2);
					window.frmStegano.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SteganoViewer() {
		initialize();
	}
	
	// ---- implementation of menu functions ----
	
	public void openImageSource() {
		
		fileChooser.setSelectedFile(new File("image-source"));
        if(fileChooser.showOpenDialog(frmStegano) != JFileChooser.APPROVE_OPTION) {
            return;  // cancelled
        }
        
        currentImage = ImageFileManager.loadImage(fileChooser.getSelectedFile());

        if(currentImage == null) {
            JOptionPane.showMessageDialog(frmStegano,
                    "The file was not in a recognized image file format.",
                    "Image Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        imageSourcePanel.setImage(currentImage);
        frmStegano.pack();
	}
	
	public void openImageToHide() {
		
		if(imageSourcePanel.getImage() == null) {
			JOptionPane.showMessageDialog(frmStegano,
                    "Open a source image first.",
                    "Image Load Error",
                    JOptionPane.ERROR_MESSAGE);
			return;
		}

		fileChooser.setSelectedFile(new File("image-to-hide"));
        if(fileChooser.showOpenDialog(frmStegano) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        currentImage = ImageFileManager.loadImage(fileChooser.getSelectedFile());

        if(currentImage == null) {
            JOptionPane.showMessageDialog(frmStegano,
                    "The file was not in a recognized image file format.",
                    "Image Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Check if both images have the same dimensions
        if(currentImage.getHeight() != imageSourcePanel.getImage().getHeight() || 
        		currentImage.getWidth() != imageSourcePanel.getImage().getWidth()) {
        	 JOptionPane.showMessageDialog(frmStegano,
                     "Both images must have the same dimensions.",
                     "Image Load Error",
                     JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        imageHidePanel.setImage(currentImage);
        frmStegano.pack();
	}
	
	public void hide() {
		
        if(imageSourcePanel.getImage() == null || imageHidePanel.getImage() == null) {
        	 JOptionPane.showMessageDialog(frmStegano,
                     "Open a source image and an image to hide.",
                     "Image Error",
                     JOptionPane.ERROR_MESSAGE);
             return;
        }
        
		Stegano s = new Stegano(imageSourcePanel.getImage(), imageHidePanel.getImage());
		s.hide();
		
		imageResultPanel.setImage(s.getResultImage());

	}
	
	public void Reveal()
	{
        if(imageSourcePanel.getImage() == null)
        {
        	 JOptionPane.showMessageDialog(frmStegano,
                     "Open a source image first.",
                     "Image Error",
                     JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        Stegano s = new Stegano(imageSourcePanel.getImage());
        s.reveal();
		imageResultPanel.setImage(s.getResultImage());
		frmStegano.pack();
	}
	
	public void saveResult()
	{
		if(imageResultPanel.getImage() == null)
		{
			JOptionPane.showMessageDialog(frmStegano,
                    "Please hide an image first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		fileChooser.setSelectedFile(new File("result.png"));
	    if (fileChooser.showSaveDialog(frmStegano) == JFileChooser.APPROVE_OPTION) {
	    	ImageFileManager.saveImage(imageResultPanel.getImage(), fileChooser.getSelectedFile());
	    }

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStegano = new JFrame();
		frmStegano.setTitle("Halftone visual cryptography");
		frmStegano.setResizable(false);
		frmStegano.setBounds(100, 100, 450, 387);
		frmStegano.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStegano.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Version 1.1");
		frmStegano.getContentPane().add(lblNewLabel, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmStegano.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		imageSourcePanel = new ImagePanel();
		tabbedPane.addTab("Image Source", imageSourcePanel);
		
		imageHidePanel = new ImagePanel();
		tabbedPane.addTab("Image to Hide", imageHidePanel);
		
		imageResultPanel = new ImagePanel();
		tabbedPane.addTab("Image Result", imageResultPanel);
	
		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("");
		frmStegano.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open Image Source");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openImageSource();
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmOpenImageTo = new JMenuItem("Open Image to Hide");
		mntmOpenImageTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openImageToHide();
			}
		});
		mnFile.add(mntmOpenImageTo);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Hide Image");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hide();
			}
		});
		mnFile.add(mntmNewMenuItem);
		
		JMenuItem mntmSaveResult = new JMenuItem("Save Result");
		mntmSaveResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveResult();
			}
		});
		
		JMenuItem mntmRevealImage = new JMenuItem("Reveal Image");
		mntmRevealImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reveal();
			}
		});
		
		mnFile.add(mntmRevealImage);
		mnFile.add(mntmSaveResult);
			
	}

}
