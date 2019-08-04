@echo off
@rem Licensed to the Apache Software Foundation (ASF) under one or more
@rem contributor license agreements.  See the NOTICE file distributed with
@rem this work for additional information regarding copyright ownership.
@rem The ASF licenses this file to You under the Apache License, Version 2.0
@rem (the "License"); you may not use this file except in compliance with
@rem the License.  You may obtain a copy of the License at
@rem
@rem     http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

if not defined DATA_SERVICE_HOME (
  set DATA_SERVICE_HOME=%~dp0..
)

if "%DATA_SERVICE_HOME:~-1%" == "\" (
  set DATA_SERVICE_HOME=%DATA_SERVICE_HOME:~0,-1%
)

if defined JAVA_HOME (
  set JAVA=%JAVA_HOME%\bin\java
) else (
  echo JAVA_HOME is not set.
  exit 1
)

if "%PROCESSOR_ARCHITECTURE%" == "x86" (
    set vs=x32
) else (
    set vs=x64
)

for %%a in (%*) do set /a args+=1

if defined args (
    set /a args=%args%
) else (
    set /a args=0
)