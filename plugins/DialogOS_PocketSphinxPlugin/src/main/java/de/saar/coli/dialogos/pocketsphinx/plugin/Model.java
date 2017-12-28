/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.saar.coli.dialogos.pocketsphinx.plugin;

import com.clt.speech.Language;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import java.util.Locale;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author koller
 */
public class Model {

    private String id;
    private String name;
    private int version;
    private String url;
    private int size;
    private Language language;
    private File acousticModelDir;
    private File dictionaryFile;
    private File languageModelFile;

    private Model(String id, String name, int version, String url, int size, Language language, File acousticModelDir, File dictionaryFile, File languageModelFile) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.url = url;
        this.size = size;
        this.language = language;
        this.acousticModelDir = acousticModelDir;
        this.dictionaryFile = dictionaryFile;
        this.languageModelFile = languageModelFile;
    }

    public static Model fromModelCache(String id, File modelDir) throws IOException, ParseException {
        File jsonFile = new File(modelDir, id + ".json");
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
        JSONObject modelRoot = (JSONObject) jsonObject.get(id);

        String name = (String) modelRoot.get("name");
        int version = (int) (long) ((Long) modelRoot.get("version"));
        String url = (String) modelRoot.get("url");
        int size = (int) (long) ((Long) modelRoot.get("size"));
        String language = (String) modelRoot.get("language");
        File acoustic = new File(modelDir, (String) modelRoot.get("acoustic"));
        File dictionary = new File(modelDir, (String) modelRoot.get("dictionary"));
        File lm = new File(modelDir, (String) modelRoot.get("lm"));

        Locale locale = Locale.forLanguageTag(language);
        Language lang = new Language(locale);

        return new Model(id, name, version, url, size, lang, acoustic, dictionary, lm);
    }

    public static Model remoteModelFromJson(JSONObject jsonObject, String id) throws ParseException {
        JSONObject modelRoot = (JSONObject) jsonObject.get(id);

        String name = (String) modelRoot.get("name");
        int version = (int) (long) ((Long) modelRoot.get("version"));
        String url = (String) modelRoot.get("url");
        int size = (int) (long) ((Long) modelRoot.get("size"));
        String language = (String) modelRoot.get("language");
        File acoustic = new File((String) modelRoot.get("acoustic"));
        File dictionary = new File((String) modelRoot.get("dictionary"));
        File lm = new File((String) modelRoot.get("lm"));

        Locale locale = Locale.forLanguageTag(language);
        Language lang = new Language(locale);

        return new Model(id, name, version, url, size, lang, acoustic, dictionary, lm);
    }
    
    private static File join(File dir, File sub) {
        Path joined = dir.toPath().resolve(sub.toPath());
        return joined.toFile();
    }

    public Model localizeRemoteModel(File modelDir) {
        File acoustic = join(modelDir, acousticModelDir);
        File dictionary = join(modelDir, dictionaryFile);
        File lm = join(modelDir, languageModelFile);
        
        return new Model(id, name, version, url, size, language, acoustic, dictionary, lm);
    }
    
    public String asJson() {
        JSONObject obj = new JSONObject();
        JSONObject modelRoot = new JSONObject();
        obj.put(id, modelRoot);
        
        modelRoot.put("name", name);
        modelRoot.put("version", version);
        modelRoot.put("url", url);
        modelRoot.put("size", size);
        modelRoot.put("language", language.getLocale().toLanguageTag());
        modelRoot.put("acoustic", acousticModelDir.getAbsolutePath());
        modelRoot.put("dictionary", dictionaryFile.getAbsolutePath());
        modelRoot.put("lm", languageModelFile.getAbsolutePath());
        
        return obj.toJSONString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    public Language getLanguage() {
        return language;
    }

    public File getAcousticModelDir() {
        return acousticModelDir;
    }

    public File getDictionaryFile() {
        return dictionaryFile;
    }

    public File getLanguageModelFile() {
        return languageModelFile;
    }

    @Override
    public String toString() {
        return name + " (" + id + " v" + version + ")";
    }

    public String getDetails() {
        return "Model{" + "id=" + id + ", name=" + name + ", version=" + version
                + ", url=" + url + ", size=" + size + ", language=" + language
                + ", acousticModelDir=" + acousticModelDir + (acousticModelDir.exists() ? " (ok)" : " (no)")
                + ", dictionaryFile=" + dictionaryFile + (dictionaryFile.exists() ? " (ok)" : " (no)")
                + ", languageModelFile=" + languageModelFile + (languageModelFile.exists() ? " (ok)" : " (no)")
                + '}';
    }
}
