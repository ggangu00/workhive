package workhive;

import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.com.utl.sim.service.EgovFileScrty;

public class EgovEnvCryptoAlgorithmCreateTest {
 
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovEnvCryptoAlgorithmCreateTest.class);
 
	//계정암호화키 키
	public String algorithmKey = "1234";
 
	//계정암호화 알고리즘(MD5, SHA-1, SHA-256)
	public String algorithm = "SHA-256";
 
	//계정암호화키 블럭사이즈
	public int algorithmBlockSize = 1024;
	
	public static void main(String[] args) throws Exception {
		EgovEnvCryptoAlgorithmCreateTest cryptoTest = new EgovEnvCryptoAlgorithmCreateTest();
 
		EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
		egovPasswordEncoder.setAlgorithm(cryptoTest.algorithm);
 
		LOGGER.info("------------------------------------------------------");
		LOGGER.info("알고리즘(algorithm) : "+cryptoTest.algorithm);
		LOGGER.info("알고리즘 키(algorithmKey) : "+cryptoTest.algorithmKey);
		LOGGER.info("알고리즘 키 Hash(algorithmKeyHash) : "+egovPasswordEncoder.encryptPassword(cryptoTest.algorithmKey));
		LOGGER.info("알고리즘 블럭사이즈(algorithmBlockSize)  :"+cryptoTest.algorithmBlockSize);
		System.out.println(EgovFileScrty.encryptPassword("1234", "USER"));
		//lZRQJQ1OROo33Kgfvg1pczGZXV0T/3WJ+343tCnIG+c=
	}
}