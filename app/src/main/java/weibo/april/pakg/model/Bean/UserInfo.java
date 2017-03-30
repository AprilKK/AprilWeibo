package weibo.april.pakg.model.Bean;

/**
 * Created by Duke on 3/29/2017.
 */

public class UserInfo {
    private String id;
    private String userId;
    private String token;
    private String refreshToken;
    private long expiresTime;
    private String phoneNum;

    public static final String ID = "_id";
    public static final String USERID="userId";
    public static final String TOKEN="Token";
    public static final String REFRESHTOKEN="RefreshToken";
    public static final String EXPIRESTIME="ExpiresTime";
    public static final String PHONENUM="PhoneNum";

    public UserInfo(String userId, String token, String refreshToken, long expiresTime, String phoneNum) {
        this.userId = userId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresTime = expiresTime;
        this.phoneNum = phoneNum;
    }

    public UserInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
