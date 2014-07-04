#!/bin/bash
javac -d bin -cp $(./classpath.sh) $(find src -type f ) 
