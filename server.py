# 1-таблица с пользователями (логин,пароль)
# 2-таблица с файлами (логин, хэшфайла(первые 256 бит),Km,Kr,вектор,остаток,длинна ключа )
import copy
import sqlite3
import socket
import threading
from PyQt5.QtWidgets import QWidget, QLabel, QComboBox, QApplication, QLineEdit, QPushButton, QMainWindow
import sys
import http.client
from PyQt5 import QtGui, Qt
lock = threading.RLock()
ip=""
port=""








class Window(QWidget):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.initwindow()

    def initwindow(self):
        self.setFont(QtGui.QFont("Times", 16, QtGui.QFont.Bold))
        self.title_log = QLabel("Сервер работает!", self)
        self.title_log.move(10, 10)
        self.setGeometry(500, 500, 300, 100)
        self.show()
        self.fun()

    def fun(self):
        global ip
        global port
        def select_login(login, pasw):
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM login WHERE login='" + login + "' AND pass='" + pasw + "'")
            records = cursor.fetchall()
            cursor.close()
            return records

        def select_file(login, hash):
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM file WHERE login='" + login + "' AND pass='" + hash + "'")
            records = cursor.fetchall()
            cursor.close()
            return records

        def insert_login(login, pasw):
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            pwd = [(login, pasw)]
            cursor.executemany("INSERT INTO login VALUES (?,?)", pwd)
            conn.commit()
            cursor.close()

        def insert_file(login, hash, Km, Kr, vector, ost, key_len):
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            text = [(login, hash, Km, Kr, vector, ost, key_len)]
            cursor.executemany("INSERT INTO file VALUES (?,?,?,?,?,?,?)", text)
            conn.commit()
            cursor.close()

        def create_reg():
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute("""CREATE TABLE login(login TEXT PRIMARY KEY, pass TEXT)""")
            conn.commit()
            cursor.close()

        def create_file():
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute(
                """CREATE TABLE file(login TEXT, hash TEXT, Km TEXT, Kr TEXT, vector TEXT, ost TEXT, key_len TEXT)""")
            conn.commit()
            cursor.close()

        def check_login():
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute("""SELECT count(name)  FROM sqlite_master WHERE type = 'table'  AND name = 'login'""")
            if cursor.fetchone()[0] == 1:
                conn.commit()
                cursor.close()
                return 0
            else:
                conn.commit()
                cursor.close()
                return 1

        def check_file():
            conn = sqlite3.connect("database.db")
            cursor = conn.cursor()
            cursor.execute("""SELECT count(name)  FROM sqlite_master WHERE type = 'table'  AND name = 'file'""")
            if cursor.fetchone()[0] == 1:
                conn.commit()
                cursor.close()
                return 0
            else:
                conn.commit()
                cursor.close()
                return 1

        def func(conn, host, port):
            lock.acquire()
            datchik = 0
            mass = []
            while datchik != 5:
                length_of_message = int.from_bytes(conn.recv(2), byteorder='big')
                msg = conn.recv(length_of_message).decode("UTF-8")
                if ("112" in msg) and (len(msg) == 3):
                    datchik = 5
                if len(msg) > 0 and (datchik == 1 or datchik == 2):
                    mass.append(copy.deepcopy(msg))
                if datchik == 0 and ("flag1" in msg) and len(msg) == 6:
                    datchik = 1
                elif datchik == 0 and ("flag2" in msg) and len(msg) == 6:
                    datchik = 2
                elif datchik == 0 and ("flag3" in msg) and len(msg) == 6:
                    datchik = 3
                elif datchik == 0 and ("flag4" in msg) and len(msg) == 6:
                    datchik = 4
            if datchik == 1:
                if check_login() == 1:
                    create_reg()
                else:
                    rec = select_login(msg[0], msg[1])
                    if len(rec) == 0:
                        rec.append("error")
                        rec.append("112")
                    else:
                        rec.append("112")

            elif datchik == 2:
                if check_file() == 1:
                    create_file()
                else:
                    rec = select_file(msg[0], msg[1])
                    if len(rec) == 0:
                        rec.append("error")
                        rec.append("112")
                    else:
                        rec.append("112")
            elif datchik == 3:
                if check_login() == 1:
                    create_reg()
                else:
                    rec = select_login(msg[0], msg[1])
                    if len(rec) == 0:
                        insert_login((msg[0], msg[1]))
                        rec.append("ok")
                        rec.append("112")
                    else:
                        rec.append("error")
                        rec.append("112")
            elif datchik == 4:
                if check_file() == 1:
                    create_file()
                else:
                    rec = select_file(msg[0], msg[1])
                    if len(rec) == 0:
                        insert_file((msg[0], msg[1], msg[2], msg[3], msg[4], msg[5], msg[6]))
                        rec.append("ok")
                        rec.append("112")
                    else:
                        rec.append("error")
                        rec.append("112")
            for i in range(0, len(rec)):
                message_to_send = rec[i].encode("UTF-8")
                conn.send(len(message_to_send).to_bytes(2, byteorder='big'))
                conn.send(message_to_send)
            lock.release()

        def f():
            global ip
            global port
            soc = socket.socket()
            print(ip)
            print(port)
            soc.bind((str(ip), int(port)))
            soc.listen(1024)
            print('Start Server')
            while True:
                conn, addr = soc.accept()
                threading.Thread(target=func, args=(conn, addr[0], addr[1])).start()

        threading.Thread(target=f).start()
class Example(QWidget):

    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        global ip
        global port
        self.setFont(QtGui.QFont("Times", 16, QtGui.QFont.Bold))
        self.label = QLabel("Адрес сервера: ", self)
        self.label.move(10, 10)

        conn = http.client.HTTPConnection("ifconfig.me")
        conn.request("GET", "/ip")
        t = conn.getresponse().read().decode("UTF-8")
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        t1=s.getsockname()[0]
        s.close()

        combo = QComboBox(self)
        combo.addItem(t)
        combo.addItem(t1)
        combo.addItem("localhost")
        combo.move(10, 50)
        combo.activated[str].connect(self.onActivated)

        self.title_log = QLabel("Порт:", self)
        self.title_log.move(10, 90)

        self.log = QLineEdit(self)
        self.log.move(10, 120)

        self.btn = QPushButton('Установить настройки', self)
        # self.btn.clicked.connect(self.button_sigin)
        self.btn.resize(self.btn.sizeHint())
        self.btn.move(10, 160)
        self.setGeometry(300, 300, 300, 200)
        self.show()

    def onActivated(self, text):
        global ip
        ip=text


class Buttons(QMainWindow):
    def show_enter(self):
        self.ex=Example()
        self.ex.btn.clicked.connect(self.button_sigin)

    def button_sigin(self):
        global port
        global ip
        port = self.ex.log.text()
        self.ex.close()
        self.window = Window()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    b = Buttons()
    b.show_enter()
    sys.exit(app.exec_())