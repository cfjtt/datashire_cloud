function isLogin() {
  // 判断是否登录
  if (localStorage['user']) {
    return true
  } else {
    return false
  }
}
function isStudent() {
  if (isLogin()) {
    return JSON.parse(localStorage['user']).role === 1
  }
  return false
}

function isTeacher() {
  if (isLogin()) {
    return JSON.parse(localStorage['user']).role === 2
  }
  return false
}

function isManager() {
  if (isLogin()) {
    return JSON.parse(localStorage['user']).role === 3
  }
  return false
}

export {isLogin, isStudent, isTeacher, isManager}
