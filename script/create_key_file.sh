#!/bin/bash

#cat 1>&2 <<-EOF
#  This script is not to run on any system.
#  iadd adds this banner to prevent script from being used.
#EOF

#exit 0

# Purpose: Clear old log file
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
_DEFAULT_APP_CONTENT_FOLDER='/var/www/html/cms_transcode_video/backend/web/uploads/'
_DEFAULT_APP_CONTENT_FOLDER='/var/www/html/vscms2/backend/web/uploads/'
_DEFAULT_APP_CONTENT_FOLDER='/var/www/html/vscms2/stream/'
_DEFAULT_APP_KEY_FOLDER='/usr/local/WowzaStreamingEngine/keys/final_test/'
_DEFAULT_APP_KEY_FOLDER='/usr/local/WowzaStreamingEngine/keys/auto_scan/'

_METHOD='cupertinostreaming-aes128-method: AES-128'
_KEY_PARAM='cupertinostreaming-aes128-key: '
_URL_PARAM='cupertinostreaming-aes128-url: '
_FORMAT='cupertinostreaming-aes128-key-format-version: 1'

_KEY="${_DEFAULT_KEY}"
_URL="${_DEFAULT_URL}"

function validate_input(){
	file_name="$1"
	key="$2"
	url="$3"

	if [[ -z "${file_name// }" ]]
	then
		echo -e "Using $0 <file_path> <key> <url>"
		exit 0
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
		#exit 1
		return 1
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

#echo -e "Using $0 <key> <url> <file_name>"

file_name="$1"
key="$2"
url="$3"

#app_content_folder="${_DEFAULT_APP_CONTENT_FOLDER}"
app_content_folder=$file_name
app_keys_folder="${_DEFAULT_APP_KEY_FOLDER}"

validate_input "$file_name" "$key" "$url" 

get_key_with_url "$key" "$url"
#create_file "$file_name" "$_METHOD\n$_KEY_PARAM${_KEY}\n$_URL_PARAM${_URL}\n$_FORMAT"
read_all_file_and_create_key "${app_content_folder}" "${app_keys_folder}"
#echo "$_KEY"
#echo "$_URL"