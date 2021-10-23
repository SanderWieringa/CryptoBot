package Rest.Responses;

public class LoginResponse {
    private String jwt;
    private boolean success;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() { return jwt; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
