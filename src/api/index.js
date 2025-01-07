import {get, post} from './http'

// 判断管理员是否登录成功
export const login = (admin, password) => get(`admin/login?admin=${admin}&password=${password}`)
export const getPassword = (admin) => get(`admin/getHashedPassword?admin=${admin}`)

export const register = (admin, password) => get(`admin/register?admin=${admin}&password=${password}`)

// ============用户相关================
// 查询用户
export const getAllUser = () => get(`user/getAll`)
// 添加用户
export const setUser = (params) => post(`user/add`, params)
export const getUserCount = () => get(`user/getCount`)
// 编辑用户

export const updatePassword = (id, password) => get(`user/updatePassword?userId=${id}&newPassword=${password}`)

// 删除用户
export const delUser = (id) => get(`user/delete?userId=${id}`)

// 根据用户id查询该用户的详细信息
export const getUserById = (id) => get(`/user/getById?userId=${id}`)
export const setUserRole = (id, role, authorId) => post(`user/setRole2?userId=${id}&role=${role}&authorId=${authorId}`)
export const setUserNameReal = (id, nameReal, authorId, authorName) => get(`user/setNameReal?userId=${id}&nameReal=${nameReal}&authorId=${authorId}&authorName=${authorName}`)

export const allPaper = () => get(`song/allSong/addPrefix`)
export const allInvisible = () => get(`song/allInvisible/addPrefix`)
export const songOfStyle = (style) => get(`song/songOfStyle?style=${style}`)
export const invisibleSong = (id) => get(`song/invisible?id=${id}`)
export const visibleSong = (id) => get(`song/visible?id=${id}`)

// =========mail
//
export const sendMail = (to) => get(`sendMail?to=${to}`)

// =======Authentication
//
export const allAuthentication = () => get(`authentication/getAll`)
export const deleteAuthentication = (id) => get(`authentication/delete?id=${id}`)

// =======Claim
//
export const allClaimUnavailable = () => get(`claim/getAllUnavailable`)
export const deleteClaim = (id) => get(`claim/delete?id=${id}`)
export const ableClaim = (id) => get(`claim/able?id=${id}`)
export const disableClaim = (id) => get(`claim/disable?id=${id}`)
// =====举报
export const allComplaint = () => get(`/complaint/getall`)
export const dealComplaint = (id, result) => get(`/complaint/deal?complaintId=${id}&result=${result}`)
// ===信息
export const addMessage = (params) => post(`/message/add`, params)
