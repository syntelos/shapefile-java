#!/bin/bash
ls lib/*.jar | tr '\n' ':' | sed 's/:$//'
