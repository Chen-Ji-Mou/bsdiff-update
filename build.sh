#!/bin/sh

set -e
set -x

ROOT=$(pwd)

rm -rf $ROOT/outputs
mkdir $ROOT/outputs

cd $ROOT/original_app
$ROOT/original_app/gradlew.bat --parallel assembleRelease
cd -

cd $ROOT/new_app
$ROOT/new_app/gradlew.bat --parallel assembleRelease
cd -