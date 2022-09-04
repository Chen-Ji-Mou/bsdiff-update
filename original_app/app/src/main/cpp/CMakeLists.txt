project(bspatch_utils)

cmake_minimum_required(VERSION 3.4.1)

aux_source_directory(bsdiff BSDIFF)
aux_source_directory(bsdiff/bzip2 BZIP)

add_library(
        bspatch_utils

        SHARED

        launch.cpp
        ${BSDIFF}
        ${BZIP})

include_directories(bsdiff bsdiff/bzip2)

target_link_libraries(
        bspatch_utils
        log)