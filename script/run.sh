DATE_FORMAT="%Y%m%d"
CURR_DATE=$(date +${DATE_FORMAT})
LOG_FILE='./log/log_'"${CURR_DATE}"'.log'

SCRIPT_CREATE_KEY_FILE='~/script/v2/create_key_file.sh'
SCRIPT_TRANSCODE_VIDEO='~/script/v2/ffmpeg_transcode.sh'
SCRIPT_GET_INFO_VIDEO='~/script/v2/ffmpeg_get_info.sh'
PATH_ROOT_CMS='/var/www/html/vscms2/video';
INPUT_FOLDER_TO_SCAN='/var/www/html/vscms2/upload/'
OUTPUT_FOLDER_ROOT_WITH_SLASH='/var/www/html/vscms2/upload/output/'
OUTPUT_FOLDER_ROOT_WITH_SLASH='/var/www/html/vscms2/stream/'
MONITOR_SERVER_PORT=6790

INPUT_FILE_TO_TEST="$1"


#/opt/java/jdk1.8.0_102/bin/java -cp .:'EncodeServer.jar':'lib/gson-2.7.jar':'lib/commons-io-2.5.jar' inet.encode.Test "$INPUT_FILE_TO_TEST" &>>${LOG_FILE} &
/opt/java/jdk1.8.0_102/bin/java -cp .:'EncodeServer.jar':'lib/gson-2.7.jar':'lib/commons-io-2.5.jar' inet.encode.Encoder "$INPUT_FOLDER_TO_SCAN" "$OUTPUT_FOLDER_ROOT_WITH_SLASH" "$SCRIPT_TRANSCODE_VIDEO" "$MONITOR_SERVER_PORT" &>>${LOG_FILE} &
