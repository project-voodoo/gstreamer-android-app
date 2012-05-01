
package org.projectvoodoo.gstandroid.gstreamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.projectvoodoo.gstandroid.Shellcmd;

public class GstPlugin implements Comparable<GstPlugin> {

    @SuppressWarnings("unused")
    private static final String TAG = "Gstreamer Android GstPlugin";

    public final String name;

    private String mDescription;
    private String mFileName;
    private String mVersion;
    private String mSourceModule;
    private String mSourceReleaseDate;
    private String mBinaryPackage;
    private String mOriginUrl;
    private License mLicense;

    private LinkedHashMap<String, GstFeature> mFeatures = new LinkedHashMap<String, GstFeature>();

    enum License {
        LGPL,
        GPL,
        UNKNOWN,
    }

    public GstPlugin(String name) {
        this.name = name;
    }

    public void loadDetails() {
        String seperator = ":\t";

        ArrayList<String> cmdout = Shellcmd.gstInspect(name);

        // parse details
        int lineNo;
        for (lineNo = 1; lineNo < cmdout.size(); lineNo++) {
            String[] splittedLine = cmdout.get(lineNo).split(seperator);
            if (splittedLine.length != 2)
                break;

            String key = splittedLine[0].trim();
            String value = splittedLine[1].trim();

            if (key.equals("Filename"))
                mFileName = value;
            else if (key.equals("Description"))
                mDescription = value;
            else if (key.equals("Filename"))
                mFileName = value;
            else if (key.equals("Version"))
                mVersion = value;
            else if (key.equals("Source module"))
                mSourceModule = value;
            else if (key.equals("Source release date"))
                mSourceReleaseDate = value;
            else if (key.equals("Binary package"))
                mBinaryPackage = value;
            else if (key.equals("Origin URL"))
                mOriginUrl = value;
        }

        // parse features
        for (; lineNo < cmdout.size(); lineNo++) {
            String[] splittedLine = cmdout.get(lineNo).split(seperator);
            if (splittedLine.length != 2)
                break;

            String featureName = splittedLine[0];
            String featureDescription = splittedLine[1];

            mFeatures.put(featureName, new GstFeature(featureName, featureDescription));
        }

    }

    public void setDetails(String filename, String version, String sourceModule,
            String sourceRelease, String binaryPackage, String originUrl, License license) {
        mFileName = filename;
        mVersion = version;
        mSourceModule = sourceModule;
        mSourceReleaseDate = sourceRelease;
        mBinaryPackage = binaryPackage;
        mOriginUrl = originUrl;
        mLicense = license;
    }

    public String getDescription() {
        if (mDescription == null)
            loadDetails();

        return mDescription;
    }

    public String getFilename() {
        if (mFileName == null)
            loadDetails();

        return mFileName;
    }

    public String getVersion() {
        if (mVersion == null)
            loadDetails();

        return mVersion;
    }

    public String getSourceModule() {
        if (mSourceModule == null)
            loadDetails();

        return mSourceModule;
    }

    public String getSourceReleaseDate() {
        if (mSourceReleaseDate == null)
            loadDetails();

        return mSourceReleaseDate;
    }

    public String getBinaryPackage() {
        if (mBinaryPackage == null)
            loadDetails();

        return mBinaryPackage;
    }

    public String getOriginUrl() {
        if (mOriginUrl == null)
            loadDetails();

        return mOriginUrl;
    }

    public License getLicense() {
        if (mLicense == null)
            loadDetails();

        return mLicense;
    }

    public void addFeature(GstFeature feature) {
        if (!mFeatures.containsKey(feature.name))
            mFeatures.put(feature.name, feature);
    }

    public void setFeatures(Collection<GstFeature> features) {
        for (GstFeature feature : features)
            addFeature(feature);
    }

    public Collection<GstFeature> getFeatures() {
        return mFeatures.values();
    }

    @Override
    public int compareTo(GstPlugin another) {
        return name.compareTo(another.name);
    }
}
