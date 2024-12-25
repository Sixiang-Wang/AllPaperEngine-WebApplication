export function formatJavaDate(dateStr)  {
    // 创建 Date 对象
    const date = new Date(dateStr);

    // 提取年份、月份、日期、小时、分钟和秒
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // 拼接成 Java 能够解析的格式
    return `${year}-${month}-${day}`;
}

export default {
    formatJavaDate
}



