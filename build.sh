#!/bin/sh

set -e
set -x

ROOT=$(pwd)

rm -rf $ROOT/outputs
mkdir $ROOT/outputs

cd $ROOT/original_app
$ROOT/original_app/gradlew --parallel assembleRelease

cd $ROOT/new_app
$ROOT/new_app/gradlew --parallel assembleRelease