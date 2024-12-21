
import json
from itertools import chain
import requests

import mysql.connector
import pyalex
from pyalex import Topics

pyalex.config.email = "1430186906@qq.com"
# 连接到 MySQL 数据库
conn = mysql.connector.connect(
    host="116.204.112.5",  # 数据库主机
    user="root",  # 数据库用户名
    password="BjMfWi6CFkrW3556",  # 数据库密码
    database="scholar",  # 数据库名
    port=3306
)

# 创建一个游标对象
cursor = conn.cursor()
with open('part_000.json', 'r', encoding='utf-8') as file:
    for line in file:
        json_data = json.loads(line)
        # print(json_data["id"])
        field_data = json.loads(json.dumps(json_data.get("field", {})))
        field_id = field_data.get("id")
        field_display_name = field_data.get("display_name")

        subfield_data = json.loads(json.dumps(json_data.get("subfield", {})))
        subfield_id = subfield_data.get("id")
        subfield_display_name = subfield_data.get("display_name")

        domain_data = json.loads(json.dumps(json_data.get("domain", {})))
        domain_id = domain_data.get("id")
        domain_display_name = domain_data.get("display_name")

        data = (
                json_data.get("id"),  # 确保是 str
                json_data.get("display_name"),  # 确保是 str
                subfield_id,
                subfield_display_name,
                field_id,
                field_display_name,
                domain_id,
                domain_display_name,
                json.dumps(json_data.get("keywords", {})),  # 转换为 JSON 字符串
                int(json_data.get("works_count", 0)) if json_data.get("cited_by_count") is not None else None,
                # 确保是 int 或 None
                json_data.get("updated_date"),
            )

    # 插入数据的 SQL 语句
        sql = """
        INSERT INTO openalex_topics (id, display_name, subfield_id, subfield_display_name, field_id, field_display_name, domain_id, domain_display_name, keywords, works_count, updated_date)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql, data)
            # 提交事务
            conn.commit()
        #         print("openalex_works数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

cursor.close()
conn.close()