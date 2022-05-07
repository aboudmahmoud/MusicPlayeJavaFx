package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Controler implements Initializable {
	/*
	 * @FXML private Pane pane;
	 */
	@FXML
	private Label lbl_Snog;
	@FXML
	private Button btn_play, btn_pause, btn_rest, btn_previous, btn_next;
	@FXML
	private ComboBox<String> Speed_comp;
	@FXML
	private Slider vloume_Slider;
	@FXML
	private ProgressBar Song_Bar;

	private Media media;
	private MediaPlayer mediaPlayer;

	private File Directory;
	private File[] files;
	private ArrayList<File> Snogs;
	private int SnogNumber;
	private int[] speed = { 25, 50, 75, 100, 125, 150, 175, 200 };
	private Timer timer;
	private TimerTask task;
	private boolean IsRuung;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Snogs = new ArrayList<File>();
		Directory = new File("musc ihave");
		files = Directory.listFiles();
		if (files != null) {

			for (File file : files) {
				Snogs.add(file);

			}
			media = new Media(Snogs.get(SnogNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			lbl_Snog.setText(Snogs.get(SnogNumber).getName());
		}

		for (int i = 0; i < speed.length; i++) {
			Speed_comp.getItems().add(Integer.toString(speed[i])+"%");

		}
		
		Speed_comp.setOnAction(this::changeSpeed);
		vloume_Slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				mediaPlayer.setVolume(vloume_Slider.getValue()*0.01);
				
			}
		});
		Song_Bar.setStyle("-fx-accent: #00FF00;");
		
		
	}

	@FXML
	public void playMedia() {
		mediaPlayer.setVolume(vloume_Slider.getValue()*0.01);
		BeginTimer();
		changeSpeed(null);
		mediaPlayer.play();
	}

	@FXML
	public void PuseMedia() {
		CancelTimer();
		mediaPlayer.pause();
	}

	@FXML
	public void resetMedia() {
		Song_Bar.setProgress(0);
		if (mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
			mediaPlayer.seek(Duration.seconds(0.0));
		}

	}

	@FXML
	public void prevaousMedia() {
		if (SnogNumber > 0) {
			SnogNumber--;
			if(IsRuung)
			{
				CancelTimer();
			}
			mediaPlayer.stop();
			media = new Media(Snogs.get(SnogNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			lbl_Snog.setText(Snogs.get(SnogNumber).getName());
			playMedia();
		} else {
			SnogNumber = (Snogs.size() - 1);
			mediaPlayer.stop();
			if(IsRuung)
			{
				CancelTimer();
			}
			media = new Media(Snogs.get(SnogNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			lbl_Snog.setText(Snogs.get(SnogNumber).getName());
			playMedia();
		}

	}

	@FXML
	public void nextMedia() {
		if (SnogNumber < Snogs.size() - 1) {
			SnogNumber++;
			mediaPlayer.stop();
			if(IsRuung)
			{
				CancelTimer();
			}
			media = new Media(Snogs.get(SnogNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			lbl_Snog.setText(Snogs.get(SnogNumber).getName());
			playMedia();
		} else {
			SnogNumber = 0;
			mediaPlayer.stop();
			if(IsRuung)
			{
				CancelTimer();
			}
			media = new Media(Snogs.get(SnogNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			lbl_Snog.setText(Snogs.get(SnogNumber).getName());
			playMedia();
		}

	}

	@FXML
	public void changeSpeed(ActionEvent event) {
 //  mediaPlayer.setRate(Integer.parseInt(Speed_comp.getValue())*0.01);
		if (Speed_comp.getValue()==null)
		{
			mediaPlayer.setRate(1);
		}
		else {
		mediaPlayer.setRate(Integer.parseInt(Speed_comp.getValue().substring(0, Speed_comp.getValue().length()-1))*0.01);
		}
		
	}

	public void BeginTimer() {
		timer =new Timer();
		task = new  TimerTask() {

			@Override
			public void run() {
				IsRuung=true ;
				double current =mediaPlayer.getCurrentTime().toSeconds();
				double end  = media.getDuration().toSeconds();
			//	System.out.println(current/end);
				Song_Bar.setProgress(current/end);
				if (current/end==1)
				{
					CancelTimer(); 
				}
				
			}
			
			
			
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
		

	}

	public void CancelTimer() {
		IsRuung=false;
		timer.cancel();

	}

}
