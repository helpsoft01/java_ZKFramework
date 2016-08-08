package com.vietek.taxioperation.util;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.vietek.taxioperation.common.AppLogger;

public class FileWatcher extends Thread {
	private final File file;

	private AtomicBoolean stop = new AtomicBoolean(false);

	public FileWatcher(File file) {
		this.file = file;
	}

	public boolean isStopped() {
		return stop.get();
	}

	public void stopThread() {
		stop.set(true);
	}

	public void doOnChange() {
		String changed = new ConfigUtil().getPropValues("NOTICE_UPDATE");
		if (changed.equals("1")) {

		}
	}

	@Override
	public void run() {
		try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
			Path path = file.toPath().getParent();
			path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

			int count = 0;

			while (!isStopped()) {
				WatchKey key;
				try {
					key = watcher.poll(25, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					return;
				}
				if (key == null) {
					Thread.yield();
					continue;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();

					switch (kind.name()) {
					case "ENTRY_MODIFY":
						if (event.count() == 1)
							count++;
						if (count == 1)
							if (filename.toString().equals(file.getName()))
								doOnChange();
						if (count == 2)
							count = 0;
						break;
					case "ENTRY_DELETE":
						break;
					case "OVERFLOW":
						Thread.yield();
						break;
					default:
						AppLogger.logDebug.info("Event not expected " + event.kind().name());
					}
					boolean valid = key.reset();
					if (!valid) {
						break;
					}
				}
				Thread.yield();
			}
		} catch (Throwable e) {
			AppLogger.logDebug.info("FileWatcher: " + e.getMessage());
		}
	}
}
