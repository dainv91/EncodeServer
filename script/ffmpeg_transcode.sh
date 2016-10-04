#!/bin/bash

#cat 1>&2 <<-EOF
#  This script is not to run on any system.
#  iadd adds this banner to prevent script from being used.
#EOF

#exit 0

# Purpose: Transcode video file to multiple format
# Author : iadd
# License: Fair license (http://www.opensource.org/licenses/fair)
# Source : https://github.com/dainv91

# Copyright (C) 2016 by iadd
#
# Usage of the works is permitted provided that this instrument is
# retained with the works, so that any entity that uses the works is
# notified of this instrument.
#
# DISCLAIMER: THE WORKS ARE WITHOUT WARRANTY.

_DEFAULT_KEY='CBD2133A519B098ED9EAD88BCA45AEFA'
_DEFAULT_URL='http://streaming.inet.vn:6789/handler'
_KEY_LENGTH=32
_DEFAULT_APP_CONTENT_FOLDER='/unica/'
_DEFAULT_APP_KEY_FOLDER='/usr/local/WowzaStreamingEngine/keys/unica_new/'
_PREFIX_CMS='/var/www/html/cms_transcode_video/backend/web/uploads'
_PREFIX_CMS='/var/www/html/vscms2/backend/web/uploads'
_PREFIX_CMS='/var/www/html/vscms2/video'
_PREFIX_CMS='/var/www/html/vscms2/video/videos/output'
_PREFIX_CMS=''

_INPUT_FOLDER_TO_SCAN='/var/www/html/vscms2/upload/'
#_PREFIX_CMS=''
_DEFAULT_VIDEO_QUALITY=720

_METHOD='cupertinostreaming-aes128-method: AES-128'
_KEY_PARAM='cupertinostreaming-aes128-key: '
_URL_PARAM='cupertinostreaming-aes128-url: '
_FORMAT='cupertinostreaming-aes128-key-format-version: 1'

_KEY="${_DEFAULT_KEY}"
_URL="${_DEFAULT_URL}"

function validate_input(){
	input_file_name="$1"
	output_folder_with_slash="$2"
	video_quality="$3"

	if [[ -z "${input_file_name// }" || -z "${output_folder_with_slash// }" ]]
	then
		echo -e "Using $0 <input_file_name> <output_folder_with_slash> <video_quality>"
		exit 1
	fi
	
	if [ ! -f "${input_file_name}" ]
	then
		echo -e "File ${input_file_name} is not valid."
		exit 1
	fi
	
	if [[ ! -z "${video_quality// }" ]]
	then
		_DEFAULT_VIDEO_QUALITY="$video_quality"
	fi	
	
	
	#if [ -e "${file_name}" ]
	#then
	#	echo "File ${file_name} existed"
		#exit 1
	#fi
}

function create_file(){
	file_name="$1";
	file_content="$2";
	
	if [ -e "${file_name}" ]
	then
		echo "File ${file_name} existed"
		exit 1
	fi
	
	mkdir -p "$(dirname ${file_name})" && echo -e "${file_content}" > "${file_name}"
	retVal="$?"
	
	if [ "$retVal" -eq 0 ]
	then
		echo -e "File ${file_name} has been created."
	fi
}

function get_key_with_url(){
	key="$1"
	url="$2"
	if [ ! -z "${key// }" ]
	then
		len="$(echo -n ${key} | wc -c)"
		if [ "$len" -ne "$_KEY_LENGTH" ]
		then
			echo 'Length of key must be '"$_KEY_LENGTH"' character...'
			exit 2
		fi
		_KEY="${key}"
	fi
	
	if [ ! -z "${url// }" ]
	then
		_URL="${url}"
	fi
}

function read_all_file_and_create_key(){
	app_content_folder_instance="$1"
	app_keys_folder="$2"
	
	app_content_folder="${_DEFAULT_APP_CONTENT_FOLDER}${app_content_folder_instance}"
	
	result=$(find "$app_content_folder" -type f)
	while IFS=' ' read -ra ADDR; do
		for i in "${ADDR[@]}"; do
			prefix="$app_content_folder"
			prefix_new="${app_keys_folder}${app_content_folder_instance}"
			key_path="${i/$prefix/$prefix_new}"
			#echo "Processing $i -- $key_path.key"
			file_name="${key_path}.key"
			create_file "$file_name" "$_METHOD\n$_KEY_PARAM${_KEY}\n$_URL_PARAM${_URL}\n$_FORMAT"
		done
	done <<< "$result"
}

function empty_output_folder_before_encode(){
    output_folder_with_slash="$1"
    rm -rf "$output_folder_with_slash"*
}

function encode_video(){
	input_file="$1"
	output_folder_with_slash="$2"
	#json_info="$3"
	q_video="$3"
	_result=""
	
        empty_output_folder_before_encode "$output_folder_with_slash"

	file_name="$(basename ${input_file})"
	file_name_without_ext="${file_name%.*}"
	file_name="${file_name_without_ext}.mp4"
	
	#f_720=" -s 1280x720 -b 3072k ${output_folder_with_slash}720_${file_name}"
	#f_480=" -s 848x480 -b 1536k ${output_folder_with_slash}480_${file_name}"
	#f_360=" -s 640x360 -b 896k ${output_folder_with_slash}360_${file_name}"
	#f_240=" -s 424x240 -b 576k ${output_folder_with_slash}240_${file_name}"
 
	f_1080=" -s 1920x1080 -b 4992k ${output_folder_with_slash}1080.mp4"
	f_720=" -s 1280x720 -b 3072k ${output_folder_with_slash}720.mp4"
	f_480=" -s 848x480 -b 1536k ${output_folder_with_slash}480.mp4"
	f_360=" -s 640x360 -b 896k ${output_folder_with_slash}360.mp4"
	f_240=" -s 424x240 -b 576k ${output_folder_with_slash}240.mp4"
	
	mkdir -p "${output_folder_with_slash}"
	retVal="$?"
	
	if [ "$retVal" -eq 0 ]
	then
		# options (-vpre old, -preset new): ultrafast, superfast, veryfast, faster, fast, medium, slow, slower, veryslow
		#common_options=' -vcodec libx264  -vpre medium -bufsize 1000k -threads 4 '
		#common_options=' -vcodec libx264  -preset medium -bufsize 1000k -threads 4 '
		#common_options=' -vcodec libx264  -vpre superfast -bufsize 1000k -threads 4 '
		#common_options=' -vcodec libx264  -vpre ultrafast -bufsize 1000k -threads 4 '
		common_options='-vcodec libx264  -preset superfast -bufsize 1000k -threads 4'
		#cmd="ffmpeg -y -i ${input_file} ${common_options} $f_720  ${common_options} $f_480 ${common_options} $f_360 ${common_options} $f_240"
		cmd="ffmpeg -y -i ${input_file} "
		for i in 240 360 480 720 1080
		do
			if [[ "$q_video" -ge "$i" ]]
			then
				_index="f_${i}"
				_result="$_result ${common_options} ${!_index}"
			fi
		done
		cmd="$cmd$_result"
		#echo  "${cmd}"
		bash -c "$cmd"
		echo 'Done...'
		process_callback "${input_file}"
		retVal="$?"
		return "$retVal"
	else
		echo -e "Cannot create folder ${output_folder_with_slash}"
		exit "$retVal"
	fi
}

function process_callback(){
	input_file="$1"
	# Mark input file done
	mv "${input_file}" "${input_file}.done"
	echo "Callback..."
}

function my_test(){
	encode_video "$1" "$2" "$3"
	exit 0
}

#echo -e "Using $0 <key> <url> <file_name>"

file_name="$1"
output_folder_with_slash="$2"
url="$3"
q_video="$3"

file_name="${_INPUT_FOLDER_TO_SCAN}${file_name}"
output_folder_with_slash="${_PREFIX_CMS}${output_folder_with_slash}"

#app_content_folder="${_DEFAULT_APP_CONTENT_FOLDER}"
app_content_folder=$file_name
app_keys_folder="${_DEFAULT_APP_KEY_FOLDER}"

validate_input "$file_name" "$output_folder_with_slash" "$q_video"
my_test "$file_name" "$output_folder_with_slash" "$_DEFAULT_VIDEO_QUALITY"

get_key_with_url "$output_folder_with_slash" "$url"
#create_file "$file_name" "$_METHOD\n$_KEY_PARAM${_KEY}\n$_URL_PARAM${_URL}\n$_FORMAT"
read_all_file_and_create_key "${app_content_folder}" "${app_keys_folder}"