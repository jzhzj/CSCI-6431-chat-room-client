package com.gwu.cs6431.service.message;

public class Message {
    private final static String EOM = "\0";
    private final static String newLine = "\r\n";
    private StartLine startLine;
    private HeaderField<Header, Status> status;
    private HeaderField<Header, String> userID;
    private HeaderField<Header, String> passwd;
    private HeaderField<Header, String> sourceUser;
    private HeaderField<Header, String> targetUser;
    private HeaderField<Header, String> sessionID;
    private String txt;

    enum Status {
        Successful, Failed, Accepted, Refused
    }

    enum StartLine {
        REG, SIGN, INVT, RSP, TXT, CLOSE, QUIT
    }

    enum Header {
        Status, UserID, Password, SourceUser, TargetUser, SessionID
    }

    private Message() {

    }

    public Message(String startLine) {
        setStartLine(startLine);
    }

    private void setStartLine(String startLine) {
        for (StartLine sl : StartLine.values()) {
            if (startLine.equalsIgnoreCase(sl.name())) {
                this.startLine = sl;
                break;
            }
        }
        if (startLine == null) {
            // TODO exception
        }
    }

    public void setStatus(String status) {
        for (Status st : Status.values()) {
            if (status.equalsIgnoreCase(st.name())) {
                this.status = new HeaderField<>(Header.Status, st);
                break;
            }
        }
        if (status == null) {
            // TODO exception
        }
    }

    public void setUserID(String userID) {
        this.userID = new HeaderField<>(Header.UserID, userID);
    }

    public void setPasswd(String passwd) {
        this.passwd = new HeaderField<>(Header.Password, passwd);
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = new HeaderField<>(Header.SourceUser, sourceUser);
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = new HeaderField<>(Header.TargetUser, targetUser);
    }

    public void setSessionID(String sessionID) {
        this.sessionID = new HeaderField<>(Header.SessionID, sessionID);
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    private void setHeader(String line) {
        String[] elements = line.split("=");
        if (elements.length != 2) {
            // TODO exception
        }
        // Status, UserID, Password, SourceUser, TargetUser, SessionID
        switch (elements[0]) {
            case "Status":
                setStatus(elements[1]);
                break;
            case "UserID":
                setUserID(elements[1]);
                break;
            case "Password":
                setPasswd(elements[1]);
                break;
            case "SourceUser":
                setSourceUser(elements[1]);
                break;
            case "TargetUser":
                setTargetUser(elements[1]);
                break;
            case "SessionID":
                setSessionID(elements[1]);
                break;
            default:
                // TODO exception
        }
    }

    public static Message genMessage(String message) {
        Message res = new Message();
        String[] lines = message.split("\\r\\n");
        // set Start line
        res.setStartLine(lines[0]);

        // set Header fields
        int i = 1;
        for (; i < lines.length && !lines[i].equals(""); i++) {
            res.setHeader(lines[i]);
        }

        // skip the line that only has <CRLF>
        i++;

        // set TXT
        StringBuilder sb = new StringBuilder();
        for (; i < lines.length && !lines[i].equals(EOM); i++) {
            sb.append(lines[i]);
            if (lines[i + 1].equals(EOM))
                break;
            sb.append(newLine);
        }
        res.setTxt(sb.toString());
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (startLine != null) {
            sb.append(startLine);
            sb.append(newLine);
        }
//        Status, UserID, Password, SourceUser, TargetUser, SessionID
        if (status != null) {
            sb.append(status);
            sb.append(newLine);
        }
        if (userID != null) {
            sb.append(userID);
            sb.append(newLine);
        }
        if (passwd != null) {
            sb.append(passwd);
            sb.append(newLine);
        }
        if (sourceUser != null) {
            sb.append(sourceUser);
            sb.append(newLine);
        }
        if (targetUser != null) {
            sb.append(targetUser);
            sb.append(newLine);
        }
        if (sessionID != null) {
            sb.append(sessionID);
            sb.append(newLine);
        }
        sb.append(newLine);
        if (txt != null && !txt.equals("")) {
            sb.append(txt);
            sb.append(newLine);
        }
        sb.append(EOM);
        return sb.toString();
    }
}
