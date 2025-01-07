import CryptoJS from 'crypto-js'

export const Md5Utils = {
  /**
   * 生成含有随机盐的密码
   */
  generate (password) {
    const randomSalt = Array.from({ length: 16 }, () =>
      Math.floor(Math.random() * 10)
    ).join('') // 生成随机盐（16位数字）
    const hash = this.md5Hex(password + randomSalt)

    let combined = ''
    for (let i = 0; i < 16; i++) {
      combined += hash[i * 2] + randomSalt[i] + hash[i * 2 + 1]
    }
    return combined
  },

  /**
   * 校验密码是否正确
   */
  verify (password, md5) {
    let hashPart = ''
    let saltPart = ''

    for (let i = 0; i < 48; i += 3) {
      hashPart += md5[i] + md5[i + 2]
      saltPart += md5[i + 1]
    }

    const calculatedHash = this.md5Hex(password + saltPart)
    return hashPart === calculatedHash
  },

  /**
   * 获取十六进制字符串形式的MD5摘要
   */
  md5Hex (src) {
    return CryptoJS.MD5(src).toString(CryptoJS.enc.Hex)
  }
}
