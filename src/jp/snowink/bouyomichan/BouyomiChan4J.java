package jp.snowink.bouyomichan;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;

public class BouyomiChan4J {

	private static final String DEFAULT_BOUYOMI_HOST = "localhost";
	private static final int DEFAULT_BOUYOMI_PORT = 50001;
	
	private String host;
	private int port;
	
	public BouyomiChan4J() {
		this.host = DEFAULT_BOUYOMI_HOST;
		this.port = DEFAULT_BOUYOMI_PORT;
	}
	
	public BouyomiChan4J(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 残りの文章をキャンセルします。
	 */
	public void clear() {
		command(host, port, 64);
	}

	/**
	 * 読み上げを一時停止します。
	 */
	public void pasuse() {
		command(host, port, 16);
	}

	/**
	 * 読み上げを再開します。
	 */
	public void resume() {
		command(host, port, 32);
	}

	/**
	 * 次の文章を読み上げます。
	 */
	public void skip() {
		command(host, port, 48);
	}

	/**
	 * 棒読みちゃんを読み上げさせます。音量・速度・音程・声質はデフォルトの設定となります。
	 * @param message 読ませたい文字列
	 */
	public void talk(String message) {
		talk(host, port, (short) -1, (short) -1, (short) -1, (short) 0, message);
	}
	

	/**
	 * 音量・速度・音程・声質を指定して棒読みちゃんを読み上げさせます。
	 * @param volume 読み上げる音量(0～100, デフォルトは-1)
	 * @param speed 読み上げる速度(50～300, デフォルトは-1)
	 * @param tone 読み上げる音程(50～200, デフォルトは-1)
	 * @param voice 読み上げる音程声質(1～8, デフォルトは0)
	 * @param message 読ませたい文字列
	 */
	public void talk(int volume, int speed, int tone, int voice, String message) {
		talk(host, port, (short) volume, (short) speed, (short) tone, (short) voice, message);
	}
	
	private void command(String host, int port, int command) {
		byte data[] = new byte[2];
		data[0] = (byte) ((command >>> 0) & 0xFF);
		data[1] = (byte) ((command >>> 8) & 0xFF);
		send(host, port, data);
	}

	private void send(String host, int port, byte[] data) {
		Socket socket = null;
		DataOutputStream out = null;
		try {
			socket = new Socket(host, port);
//			System.out.println("接続しました" + socket.getRemoteSocketAddress());
			out = new DataOutputStream(socket.getOutputStream());
			out.write(data);
		} catch (ConnectException e) {
			System.out.println("接続できませんでした");
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (socket != null) {
					socket.close();
//					System.out.println("切断します");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//			System.out.println("終了します");
		}
	}

	private void talk(String host, int port, short volume, short speed, short tone, short voice, String message) {
			byte messageData[] = null;
			try {
				messageData = message.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int length = messageData.length;
			byte data[] = new byte[15 + length];
			data[0] = (byte) 1;	 // コマンド 1桁目
			data[1] = (byte) 0;	 // コマンド 2桁目
			data[2] = (byte) ((speed >>> 0) & 0xFF); // 速度 1桁目
			data[3] = (byte) ((speed >>> 8) & 0xFF); // 速度 2桁目
			data[4] = (byte) ((tone >>> 0) & 0xFF); // 音程 1桁目
			data[5] = (byte) ((tone >>> 8) & 0xFF); // 音程 2桁目
			data[6] = (byte) ((volume >>> 0) & 0xFF); // 音量 1桁目
			data[7] = (byte) ((volume >>> 8) & 0xFF); // 音量 2桁目
			data[8] = (byte) ((voice >>> 0) & 0xFF); // 声質 1桁目
			data[9] = (byte) ((voice >>> 8) & 0xFF); // 声質 2桁目
			data[10] = (byte) 0; // エンコード(0: UTF-8)
			data[11] = (byte) ((length >>> 0) & 0xFF); // 長さ 1桁目
			data[12] = (byte) ((length >>> 8) & 0xFF); // 長さ 2桁目
			data[13] = (byte) ((length >>> 16) & 0xFF);	 // 長さ 3桁目
			data[14] = (byte) ((length >>> 24) & 0xFF); // 長さ 4桁目			
			System.arraycopy(messageData, 0, data, 15, length);
			send(host, port, data);
	}
	
}
