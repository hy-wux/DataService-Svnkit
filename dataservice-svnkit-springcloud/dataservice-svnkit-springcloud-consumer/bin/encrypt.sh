#!/bin/bash
#
# Copyright 2011 The Apache Software Foundation
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# 判断是否安装了Java
if [ -n "${JAVA_HOME}" ]; then
  JAVA="${JAVA_HOME}/bin/java"
else
  if [ `command -v java` ]; then
    JAVA="java"
  else
    echo "JAVA_HOME is not set" >&2
    exit 1
  fi
fi

# 判断环境变量
if [ -z "${DATA_SERVICE_HOME}" ]; then
  export DATA_SERVICE_HOME="$(cd "`dirname "$0"`"/..; pwd)"
  export PATH=$PATH:${DATA_SERVICE_HOME}/bin
fi

# 引入依赖的Jar包。
JARS_DIR="${DATA_SERVICE_HOME}/lib"

#执行Main方法
"$JAVA" -Xmx128m -classpath ${DATA_SERVICE_HOME}/config:${JARS_DIR}/* -Dloader.path=${DATA_SERVICE_HOME}/config/,${DATA_SERVICE_HOME}/lib/ com.service.framework.core.encrypt.EncryptorApp "$@"