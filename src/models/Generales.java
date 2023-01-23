package models;

public class Generales {

	private String url;

	private int downloaded;

	/**
	 * Creates a general video
	 * 
	 * @param url        String
	 * @param downloaded boolean
	 */
	public Generales(String url, int downloaded) {
		super();
		this.url = url;
		this.downloaded = downloaded;
	}

	/**
	 * Creates a general video that is not downloaded
	 * 
	 * @param url String
	 */
	public Generales(String url) {
		super();
		this.url = url;
		downloaded = 0;
	}

	public String getUrl() {
		return url;
	}

	public boolean isDownloaded() {
		return downloaded == 0;
	}

}
