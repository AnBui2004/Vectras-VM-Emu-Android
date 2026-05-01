cmake_minimum_required(VERSION 3.22.1)
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")
project("native_helper")

add_library(native_helper SHARED cpu-info.cpp)

find_library(log-lib log)
target_link_libraries(native_helper ${log-lib})