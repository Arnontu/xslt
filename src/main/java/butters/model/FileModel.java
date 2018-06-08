package butters.model;

import java.net.URL;
import java.nio.file.Path;

public class FileModel {
	private Path localPath;
	private String downloadUrl;
	private String type;
	public Path getLocalPath() {
		return localPath;
	}
	public void setLocalPath(Path localPath) {
		this.localPath = localPath;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downlaodUrl) {
		this.downloadUrl = downlaodUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String path) {
		String type = "FILE";
		if (path.endsWith("xml")) type = "XML";
		else if (path.endsWith("xsl") || path.endsWith("xslt")) type = "XSLT";
		
		this.type = type;
	}
}
