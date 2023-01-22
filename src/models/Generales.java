package models;

public class Generales {

	private long id;

	private String url;

	private boolean downloaded;

	public Generales(long id, String url, boolean downloaded) {
		super();
		this.id = id;
		this.url = url;
		this.downloaded = downloaded;
	}

	public long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

}
