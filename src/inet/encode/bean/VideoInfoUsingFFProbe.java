package inet.encode.bean;

import inet.encode.bean.info.Stream;
import inet.encode.bean.info.Format;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import inet.util.JsonUtil;
import java.lang.reflect.Type;

public class VideoInfoUsingFFProbe {

    @SerializedName("streams")
    @Expose
    private List<Stream> streams = new ArrayList<Stream>();
    @SerializedName("format")
    @Expose
    private Format format;

    /**
     *
     * @return The streams
     */
    public List<Stream> getStreams() {
        return streams;
    }

    /**
     *
     * @param streams The streams
     */
    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    /**
     *
     * @return The format
     */
    public Format getFormat() {
        return format;
    }

    /**
     *
     * @param format The format
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    public static VideoInfoUsingFFProbe parseJson(String json) {
        Type type = new TypeToken<VideoInfoUsingFFProbe>() {
        }.getType();
        VideoInfoUsingFFProbe obj = JsonUtil.fromJson(json, type);
        return obj;
    }

    public Stream getVideoStreamInFile(boolean isVideo) {
        final String videoCodecType = "video";
        final String audioCodecType = "audio";
        Stream obj = null;
        List<Stream> lst = this.getStreams();
        if (lst != null && lst.size() > 0) {
            for (Stream s : lst) {
                if (isVideo) {
                    if (s != null && videoCodecType.equalsIgnoreCase(s.getCodecType())) {
                        obj = s;
                        break;
                    }
                } else if (s != null && audioCodecType.equalsIgnoreCase(s.getCodecType())) {
                    obj = s;
                    break;
                }

            }
        }
        return obj;
    }
}
