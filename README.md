# MyXmlTest
将一定格式的xml文件，转换为特定格式的cfg配置文件
如xml:
<?xml version="1.0" encoding="GB2312" ?>
<Partition_Info>
<Part Sel="1" PartitionName="boot" FlashType="emmc" FileSystem="none" Start="0" Length="4M" SelectFile="fastboot.bin"/>
<Part Sel="1" PartitionName="bootargs" FlashType="emmc" FileSystem="none" Start="4M" Length="4M" SelectFile="bootargs.bin"/>
<Part Sel="1" PartitionName="hwconfig" FlashType="emmc" FileSystem="none" Start="8M" Length="4M" SelectFile="hwconfig.bin"/>
<Part Sel="0" PartitionName="loader" FlashType="emmc" FileSystem="none" Start="12M" Length="16M" SelectFile=""/>
<Part Sel="1" PartitionName="recovery" FlashType="emmc" FileSystem="none" Start="28M" Length="24M" SelectFile="recovery.img"/>
<Part Sel="0" PartitionName="caverify" FlashType="emmc" FileSystem="none" Start="52M" Length="4M" SelectFile=""/>
<Part Sel="1" PartitionName="logo" FlashType="emmc" FileSystem="none" Start="56M" Length="12M" SelectFile="logo.img"/>
<Part Sel="0" PartitionName="ipdata" FlashType="emmc" FileSystem="none" Start="68M" Length="4M" SelectFile="ipdata.bin"/>
<Part Sel="0" PartitionName="eeprom" FlashType="emmc" FileSystem="none" Start="72M" Length="8M" SelectFile=""/>
<Part Sel="0" PartitionName="cadata" FlashType="emmc" FileSystem="none" Start="80M" Length="4M" SelectFile=""/>
<Part Sel="0" PartitionName="ldflage" FlashType="emmc" FileSystem="none" Start="84M" Length="4M" SelectFile="loaderflag.bin"/>
<Part Sel="1" PartitionName="baseparam" FlashType="emmc" FileSystem="none" Start="88M" Length="4M" SelectFile="baseparam.img"/>
<Part Sel="0" PartitionName="fastplay" FlashType="emmc" FileSystem="none" Start="92M" Length="32M" SelectFile=""/>
<Part Sel="0" PartitionName="misc" FlashType="emmc" FileSystem="none" Start="124M" Length="4M" SelectFile=""/>
<Part Sel="0" PartitionName="blackbox" FlashType="emmc" FileSystem="none" Start="128M" Length="8M" SelectFile=""/>
<Part Sel="0" PartitionName="factory" FlashType="emmc" FileSystem="none" Start="136M" Length="36M" SelectFile="factory.bin"/>
<Part Sel="1" PartitionName="kernel" FlashType="emmc" FileSystem="none" Start="172M" Length="16M" SelectFile="kernel.img"/>
<Part Sel="1" PartitionName="system" FlashType="emmc" FileSystem="ext3/4" Start="188M" Length="1024M" SelectFile="system.ext4"/>
<Part Sel="1" PartitionName="userdata" FlashType="emmc" FileSystem="ext3/4" Start="1212M" Length="4664M" SelectFile="userdata.ext4"/>
<Part Sel="1" PartitionName="cache" FlashType="emmc" FileSystem="ext3/4" Start="5876M" Length="1024M" SelectFile="cache.ext4"/>
</Partition_Info>

转换为cfg:
mtd_name	mtd_type	mtd_version	start_address	fs_path	mtd_file size
boot	3	20201	0x0	no	fastboot.bin	0x400000
bootargs	3	20201	0x400000	no	bootargs.bin	0x400000
hwconfig	3	20201	0x800000	no	hwconfig.bin	0x400000
loader	3	20201	0xc00000	no	null	0x1000000
recovery	3	20201	0x1c00000	no	recovery.img	0x1800000
caverify	3	20201	0x3400000	no	null	0x400000
logo	3	20201	0x3800000	no	logo.img	0xc00000
ipdata	3	20201	0x4400000	no	ipdata.bin	0x400000
eeprom	3	20201	0x4800000	no	null	0x800000
cadata	3	20201	0x5000000	no	null	0x400000
ldflage	3	20201	0x5400000	no	loaderflag.bin	0x400000
baseparam	3	20201	0x5800000	no	baseparam.img	0x400000
fastplay	3	20201	0x5c00000	no	null	0x2000000
misc	3	20201	0x7c00000	no	null	0x400000
blackbox	3	20201	0x8000000	no	null	0x800000
factory	3	20201	0x8800000	no	factory.bin	0x2400000
kernel	3	20201	0xac00000	no	kernel.img	0x1000000
system	3	20201	0xbc00000	no	system.ext4	0x40000000
userdata	3	20201	0x4bc00000	no	userdata.ext4	0x123800000
cache	3	20201	0x16f400000	no	cache.ext4	0x40000000

用法：java环境下，命令行
javac DoXml.java  //编译生成三个文件，在生成的文件中执行以下命令
java DoXml 需要解析的xml文件或文件夹
