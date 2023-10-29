package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Flag definitions */
    public static final Flag FLAG_NAME = new Flag("name");
    public static final Flag FLAG_PHONE = new Flag("phone");
    public static final Flag FLAG_EMAIL = new Flag("email");
    public static final Flag FLAG_ADDRESS = new Flag("addr");
    public static final Flag FLAG_TAG = new Flag("tag");
    public static final Flag FLAG_ORGANIZATION = new Flag("org");
    public static final Flag FLAG_RECRUITER = new Flag("rec");
    public static final Flag FLAG_URL = new Flag("url");
    public static final Flag FLAG_STATUS = new Flag("stat");
    public static final Flag FLAG_POSITION = new Flag("pos");
    public static final Flag FLAG_ID = new Flag("id");
    public static final Flag FLAG_RECURSIVE = new Flag("recursive");
    public static final Flag FLAG_ORGANIZATION_ID = new Flag("oid");
    public static final Flag FLAG_RECRUITER_ID = new Flag("rid");
    public static final Flag FLAG_TITLE = new Flag("title");
    public static final Flag FLAG_DEADLINE = new Flag("by");
    public static final Flag FLAG_STAGE = new Flag("stage");
    public static final Flag FLAG_DESCRIPTION = new Flag("desc");
    public static final Flag FLAG_NOT_APPLIED = new Flag("toapply");
    public static final Flag FLAG_STALE = new Flag("stale");
    public static final Flag FLAG_CSV = new Flag("csv");
    public static final Flag FLAG_JSON = new Flag("json");


}
