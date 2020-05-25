package ar.edu.unlam.eia.ml.redes;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.util.ArchiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FF {
    protected static final Logger log = LoggerFactory.getLogger(org.deeplearning4j.base.MnistFetcher.class);
    protected File BASE_DIR = new File(System.getProperty("user.home"));
    protected static final String LOCAL_DIR_NAME = "MNIST";
    protected File FILE_DIR;
    private File fileDir;
    private static final String trainingFilesURL = "http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz";
    private static final String trainingFilesMD5 = "f68b3c2dcbeaaa9fbdd348bbdeb94873";
    private static final String trainingFilesFilename = "train-images-idx3-ubyte.gz";
    public static final String trainingFilesFilename_unzipped = "train-images-idx3-ubyte";
    private static final String trainingFileLabelsURL = "http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz";
    private static final String trainingFileLabelsMD5 = "d53e105ee54ea40749a09fcbcd1e9432";
    private static final String trainingFileLabelsFilename = "train-labels-idx1-ubyte.gz";
    public static final String trainingFileLabelsFilename_unzipped = "train-labels-idx1-ubyte";
    private static final String testFilesURL = "http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz";
    private static final String testFilesMD5 = "9fb629c4189551a2d022fa330f9573f3";
    private static final String testFilesFilename = "t10k-images-idx3-ubyte.gz";
    public static final String testFilesFilename_unzipped = "t10k-images-idx3-ubyte";
    private static final String testFileLabelsURL = "http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz";
    private static final String testFileLabelsMD5 = "ec29112dd5afa0611ce80d1b7f02629c";
    private static final String testFileLabelsFilename = "t10k-labels-idx1-ubyte.gz";
    public static final String testFileLabelsFilename_unzipped = "t10k-labels-idx1-ubyte";

    public String getName() {
        return "MNIST";
    }

    public File getBaseDir() {
        return this.FILE_DIR;
    }

    public String getTrainingFilesURL() {
        return "http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz";
    }

    public String getTrainingFilesMD5() {
        return "f68b3c2dcbeaaa9fbdd348bbdeb94873";
    }

    public String getTrainingFilesFilename() {
        return "train-images-idx3-ubyte.gz";
    }

    public String getTrainingFilesFilename_unzipped() {
        return "train-images-idx3-ubyte";
    }

    public String getTrainingFileLabelsURL() {
        return "http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz";
    }

    public String getTrainingFileLabelsMD5() {
        return "d53e105ee54ea40749a09fcbcd1e9432";
    }

    public String getTrainingFileLabelsFilename() {
        return "train-labels-idx1-ubyte.gz";
    }

    public String getTrainingFileLabelsFilename_unzipped() {
        return "train-labels-idx1-ubyte";
    }

    public String getTestFilesURL() {
        return "http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz";
    }

    public String getTestFilesMD5() {
        return "9fb629c4189551a2d022fa330f9573f3";
    }

    public String getTestFilesFilename() {
        return "t10k-images-idx3-ubyte.gz";
    }

    public String getTestFilesFilename_unzipped() {
        return "t10k-images-idx3-ubyte";
    }

    public String getTestFileLabelsURL() {
        return "http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz";
    }

    public String getTestFileLabelsMD5() {
        return "ec29112dd5afa0611ce80d1b7f02629c";
    }

    public String getTestFileLabelsFilename() {
        return "t10k-labels-idx1-ubyte.gz";
    }

    public String getTestFileLabelsFilename_unzipped() {
        return "t10k-labels-idx1-ubyte";
    }

    public File downloadAndUntar() throws IOException {
        if (this.fileDir != null) {
            return this.fileDir;
        } else {
            File baseDir = this.getBaseDir();
            if (!baseDir.isDirectory() && !baseDir.mkdir()) {
                throw new IOException("Could not mkdir " + baseDir);
            } else {
                log.info("Downloading {}...", this.getName());
                File tarFile = new File(baseDir, this.getTrainingFilesFilename());
                File testFileLabels = new File(baseDir, this.getTestFilesFilename());
                this.tryDownloadingAFewTimes(new URL(this.getTrainingFilesURL()), tarFile, this.getTrainingFilesMD5());
                this.tryDownloadingAFewTimes(new URL(this.getTestFilesURL()), testFileLabels, this.getTestFilesMD5());
                ArchiveUtils.unzipFileTo(tarFile.getAbsolutePath(), baseDir.getAbsolutePath());
                ArchiveUtils.unzipFileTo(testFileLabels.getAbsolutePath(), baseDir.getAbsolutePath());
                File labels = new File(baseDir, this.getTrainingFileLabelsFilename());
                File labelsTest = new File(baseDir, this.getTestFileLabelsFilename());
                this.tryDownloadingAFewTimes(new URL(this.getTrainingFileLabelsURL()), labels, this.getTrainingFileLabelsMD5());
                this.tryDownloadingAFewTimes(new URL(this.getTestFileLabelsURL()), labelsTest, this.getTestFileLabelsMD5());
                ArchiveUtils.unzipFileTo(labels.getAbsolutePath(), baseDir.getAbsolutePath());
                ArchiveUtils.unzipFileTo(labelsTest.getAbsolutePath(), baseDir.getAbsolutePath());
                this.fileDir = baseDir;
                return this.fileDir;
            }
        }
    }

    private void tryDownloadingAFewTimes(URL url, File f, String targetMD5) throws IOException {
        this.tryDownloadingAFewTimes(0, url, f, targetMD5);
    }

    private void tryDownloadingAFewTimes(int attempt, URL url, File f, String targetMD5) throws IOException {
        int maxTries = 3;
        boolean isCorrectFile = f.isFile();
        if (attempt < maxTries && !isCorrectFile) {
            FileUtils.copyURLToFile(url, f);
            if (!this.checkMD5OfFile(targetMD5, f)) {
                f.delete();
                this.tryDownloadingAFewTimes(attempt + 1, url, f, targetMD5);
            }
        } else if (!isCorrectFile) {
            throw new IOException("Could not download " + url.getPath() + "\n properly despite trying " + maxTries + " times, check your connection. File info:\nTarget MD5: " + targetMD5 + "\nHash matches: " + this.checkMD5OfFile(targetMD5, f) + "\nIs valid file: " + f.isFile());
        }

    }

    private boolean checkMD5OfFile(String targetMD5, File file) throws IOException {
        InputStream in = FileUtils.openInputStream(file);
        String trueMd5 = DigestUtils.md5Hex(in);
        IOUtils.closeQuietly(in);
        return targetMD5.equals(trueMd5);
    }

    public static void gunzipFile(File baseDir, File gzFile) throws IOException {
        log.info("gunzip'ing File: " + gzFile.toString());
        Process p = Runtime.getRuntime().exec(String.format("gunzip %s", gzFile.getAbsolutePath()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        log.info("Here is the standard error of the command (if any):\n");

        String s;
        while((s = stdError.readLine()) != null) {
            log.info(s);
        }

        stdError.close();
    }

    public File getBASE_DIR() {
        return this.BASE_DIR;
    }

    public File getFILE_DIR() {
        return this.FILE_DIR;
    }

    public File getFileDir() {
        return this.fileDir;
    }

    public void setBASE_DIR(File BASE_DIR) {
        this.BASE_DIR = BASE_DIR;
    }

    public void setFILE_DIR(File FILE_DIR) {
        this.FILE_DIR = FILE_DIR;
    }

    public void setFileDir(File fileDir) {
        this.fileDir = fileDir;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof org.deeplearning4j.base.MnistFetcher)) {
            return false;
        } else {
            org.deeplearning4j.base.MnistFetcher other = (org.deeplearning4j.base.MnistFetcher)o;
            
            

                Object this$FILE_DIR = this.getFILE_DIR();
                Object other$FILE_DIR = other.getFILE_DIR();
                if (this$FILE_DIR == null) {
                    if (other$FILE_DIR != null) {
                        return false;
                    }
                } else if (!this$FILE_DIR.equals(other$FILE_DIR)) {
                    return false;
                }

                Object this$fileDir = this.getFileDir();
                Object other$fileDir = other.getFileDir();
                if (this$fileDir == null) {
                    if (other$fileDir != null) {
                        return false;
                    }
                } else if (!this$fileDir.equals(other$fileDir)) {
                    return false;
                }

                return true;
            }
        
    }

    protected boolean canEqual(Object other) {
        return other instanceof org.deeplearning4j.base.MnistFetcher;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $BASE_DIR = this.getBASE_DIR();
        int r = result * 59 + ($BASE_DIR == null ? 43 : $BASE_DIR.hashCode());
        Object $FILE_DIR = this.getFILE_DIR();
        r = r * 59 + ($FILE_DIR == null ? 43 : $FILE_DIR.hashCode());
        Object $fileDir = this.getFileDir();
        r = r * 59 + ($fileDir == null ? 43 : $fileDir.hashCode());
        return r;
    }

    public String toString() {
        return "MnistFetcher(BASE_DIR=" + this.getBASE_DIR() + ", FILE_DIR=" + this.getFILE_DIR() + ", fileDir=" + this.getFileDir() + ")";
    }

    public FF() {
        this.FILE_DIR = new File(this.BASE_DIR, "MNIST");
    }
}
