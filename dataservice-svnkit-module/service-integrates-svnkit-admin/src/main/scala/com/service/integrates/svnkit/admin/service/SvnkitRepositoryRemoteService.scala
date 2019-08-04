package com.service.integrates.svnkit.admin.service

import java.util

import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.{SVNDirEntry, SVNException, SVNNodeKind}

trait SvnkitRepositoryRemoteService {
  @throws[SVNException]
  def createSubDirectory(username: String, password: String, url: String, paths: util.List[String])

  @throws[SVNException]
  def listEntries(repository: SVNRepository, path: String, kind: SVNNodeKind, recursive: Boolean): util.List[SVNDirEntry]

  @throws[SVNException]
  def listEntries(username: String, password: String, url: String, path: String, kind: SVNNodeKind, recursive: Boolean): util.List[SVNDirEntry]
}
