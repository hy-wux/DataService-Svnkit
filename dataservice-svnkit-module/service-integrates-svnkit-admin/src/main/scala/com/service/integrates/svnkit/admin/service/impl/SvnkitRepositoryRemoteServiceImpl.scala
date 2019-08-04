package com.service.integrates.svnkit.admin.service.impl

import java.util

import com.service.framework.core.utils.CommUtil
import com.service.integrates.svnkit.admin.service.SvnkitRepositoryRemoteService
import org.springframework.stereotype.Service
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.io.{SVNRepository, SVNRepositoryFactory}
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.tmatesoft.svn.core.{SVNDirEntry, SVNException, SVNNodeKind, SVNURL}

import scala.collection.JavaConversions._

@Service
class SvnkitRepositoryRemoteServiceImpl extends SvnkitRepositoryRemoteService {
  @throws[SVNException]
  private def createDefaultAuthenticationManager(url: String, username: String, password: String): SVNRepository = {
    val repository: SVNRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url))
    val svnAuthenticationManager: ISVNAuthenticationManager = SVNWCUtil.createDefaultAuthenticationManager(username, (if (CommUtil.isEmpty(password)) "" else password).toCharArray)
    repository.setAuthenticationManager(svnAuthenticationManager)
    repository
  }

  @throws[SVNException]
  def createSubDirectory(username: String, password: String, url: String, paths: util.List[String]): Unit = {
    val repository: SVNRepository = createDefaultAuthenticationManager(url, username, password)
    val editor = repository.getCommitEditor("创建目录", null, true, null)
    editor.openRoot(-1)
    paths.foreach(path => {
      editor.addDir(path, null, -1)
      editor.closeDir()
    })
    editor.closeDir()
    editor.closeEdit()
  }

  @throws[SVNException]
  def listEntries(repository: SVNRepository, path: String, kind: SVNNodeKind, recursive: Boolean): util.List[SVNDirEntry] = {
    val entries = repository.getDir(path, -1, null, null.asInstanceOf[util.Collection[_]])
    val result: util.List[SVNDirEntry] = new util.ArrayList[SVNDirEntry]()
    entries.map(_.asInstanceOf[SVNDirEntry]).foreach(entry => {
      if (CommUtil.isNull(kind) || entry.getKind == kind) {
        result.add(entry)
      }
      if (recursive && entry.getKind == SVNNodeKind.DIR) {
        result.addAll(listEntries(repository, if (CommUtil.isEmpty(path)) entry.getName else s"${path}/${entry.getName}", kind, recursive))
      }
    })
    result
  }

  @throws[SVNException]
  def listEntries(username: String, password: String, url: String, path: String, kind: SVNNodeKind, recursive: Boolean): util.List[SVNDirEntry] = {
    val repository: SVNRepository = createDefaultAuthenticationManager(url, username, password)
    val result: util.List[SVNDirEntry] = new util.ArrayList[SVNDirEntry]()
    result.addAll(listEntries(repository, path, kind, recursive))
    result
  }
}
