package william.wyatt.savingsamuel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class HighScoresManager {
	public static final int SCORES_KEPT = 10;
	private static HighScoresManager hInstance;
	private static File fFileDir;
	
	public static void Init(File file) {
		fFileDir = file;
		hInstance = new HighScoresManager();
	}
	
	private HighScore[] hScores;
	
	public HighScoresManager() {
		hScores = new HighScore[4];
		for(int i = 0; i < 4; i++) {
			File tmp = new File(fFileDir, "scores" + i);
			hScores[i] = this.new HighScore(tmp);
		}
	}
	
	public static int[] getScores(int board) {
		if(board < 0 || board > 3)
			return null;
		return hInstance.hScores[board].getScores();
	}
	public static void addScore(int board, int score) {
		if(board < 0 || board > 3)
			return;
		hInstance.hScores[board].addScore(score);
	}
	public static void clearScores() {
		for(int i = 0; i < 4; i++) {
			hInstance.hScores[i].clearScores();
		}
	}
	
	public class HighScore {
		private File fFile;
		private int[] iScores;
		
		public HighScore(File file) {
			fFile = file;
			iScores = new int[HighScoresManager.SCORES_KEPT];
			loadScores();
		}
		
		private void loadScores() {
			if(!fFile.isFile())
				return;
			InputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(fFile));
				byte[] buffer = new byte[4];
				for(int i = 0; i < HighScoresManager.SCORES_KEPT; i++) {
					if(in.read(buffer) == -1)
						break;
				
					ByteBuffer bb = ByteBuffer.wrap(buffer);
					iScores[i] = bb.getInt();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		private void saveScores() {
			OutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(fFile));
				for(int i = 0; i < HighScoresManager.SCORES_KEPT; i++) {
					out.write(ByteBuffer.allocate(4).putInt(iScores[i]).array());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}

		public void addScore(int score) {
			for(int i = 0; i < HighScoresManager.SCORES_KEPT; i++) {
				if(score > iScores[i]) {
					for(int j = HighScoresManager.SCORES_KEPT - 1; j > i; j--) {
						iScores[j] = iScores[j - 1];
					}
					iScores[i] = score;
					saveScores();
					return;
				}
			}
		}
		public int[] getScores() {
			return iScores;
		}
		public void clearScores() {
			fFile.delete();
			iScores = new int[HighScoresManager.SCORES_KEPT];
		}
	}
}
