
package org.projectvoodoo.gstandroid.gstreamer;

public class GstFeature {

    public final String name;
    public final String longName;

    private String mDescription;
    private String mAuthors;
    private String mRank;

    private String mMore;

    public GstFeature(String name, String longName) {
        this.name = name;
        this.longName = longName;
    }

}
