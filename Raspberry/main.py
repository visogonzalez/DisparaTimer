import network
import time
import socket
from machine import Pin

r1=Pin(16,Pin.OUT)
r2=Pin(17,Pin.OUT)
led2= Pin("LED", Pin.OUT)
led2.on()
r1.off()
r2.on()

# if you do not see the network you may have to power cycle
# unplug your pico w for 10 seconds and plug it in again
def ap_mode(ssid, password):
    """
        Description: This is a function to activate AP mode

        Parameters:

        ssid[str]: The name of your internet connection
        password[str]: Password for your internet connection

        Returns: Nada
    """
    # Just making our internet connection
    ap = network.WLAN(network.AP_IF)
    ap.config(essid=ssid, password=password, pm = 0x111022)
    ap.active(True)

    while ap.active() == False:
        pass
    print('AP Mode Is Active, You can Now Connect')
    print('IP Address To Connect to:: ' + ap.ifconfig()[0])

    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s2.bind(('', 5005))

    while True:
      data, addre = s2.recvfrom(1024)
      r1.toggle()
      r2.toggle()

ap_mode('DISPARA','DISPARAFILM')
