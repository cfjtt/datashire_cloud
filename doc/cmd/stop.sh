#!/bin/bash
ps -ef |grep platform.jar |grep -v grep |awk '{print "kill -9 "$2}'|sh
