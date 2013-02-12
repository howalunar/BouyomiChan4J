package org.snowink.bouyomichan;

public class BouyomiSample {

	public static void main(String[] args) {
		BouyomiChan4J bouyomi = new BouyomiChan4J();
		bouyomi.talk("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめも");
		bouyomi.talk(-1, -1, 80, 2, "前の文は途中でスキップされました");
		bouyomi.talk("この文は読まれません");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bouyomi.pasuse();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bouyomi.resume();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bouyomi.skip();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bouyomi.clear();
	}

}
