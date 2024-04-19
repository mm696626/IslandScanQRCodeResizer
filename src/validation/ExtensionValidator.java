package validation;

public class ExtensionValidator {
    public boolean isExtensionValid(String fileName) {
        fileName = fileName.toLowerCase();
        return fileName.endsWith("png") || fileName.endsWith("jpg") || fileName.endsWith("jpeg");
    }
}
