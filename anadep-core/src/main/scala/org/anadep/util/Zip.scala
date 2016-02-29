package org.anadep.util

import java.io.File
import java.io.InputStream
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

class Zip(file: File) {
  val zipFile = new ZipFile(file)

  def foreach(f: InputStream => Unit) {
    foreach(_ => true, f)
  }

  def foreach(entryNameFilter: String => Boolean, f: InputStream => Unit) {
    val zipEntries = zipFile.entries()
    while (zipEntries.hasMoreElements) {
      val zipEntry = zipEntries.nextElement
      if (entryNameFilter(zipEntry.getName)) {
        f(zipFile.getInputStream(zipEntry))
      }
    }
  }
}
