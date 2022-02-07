import styled from "styled-components";
import { useForm } from "react-hook-form";
import { fetchGroup } from "../../api/CreateMoim";
import { useRecoilValue } from "recoil";
import { userIdAtom } from "../../atoms/Login";

export interface IMoimForm {
  groupNo: string;
  productName: string;
  productOriginalPrice: number;
  productPrice: number;
  productDetail: string;
  productLink: string;

  groupLeader: string;
  maxPeople: number;
  deadline: string; // *공구 마감 날짜
  status: string; // *공구 진행 상태
}

function CreateMoim() {
  const userId = useRecoilValue(userIdAtom);
  // console.log(userId);
  // console.log(typeof userId);

  const {
    register,
    watch,
    handleSubmit,
    formState: { errors },
    setError,
    getValues,
  } = useForm<IMoimForm>();

  const onValid = (data: IMoimForm) => {
    console.log(data); // data 확인

    const newDeadline =
      data.deadline.replace("T", " ").substring(0, 19) + ":00";

    const newData = {
      ...data,
      deadline: newDeadline,
      status: "모집중",
      groupLeader: userId,
    };

    fetchGroup(newData);
  };

  return (
    <MoimForm onSubmit={handleSubmit(onValid)}>
      <FormTitle>모임 생성</FormTitle>
      <InputDiv>
        <InputTitle>제목</InputTitle>
        <Input
          {...register("productName", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.productName?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>출시가</InputTitle>
        <Input
          {...register("productOriginalPrice", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.productOriginalPrice?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>공구가</InputTitle>
        <Input
          {...register("productPrice", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.productPrice?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>내용</InputTitle>
        <Input
          {...register("productDetail", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.productDetail?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>URL</InputTitle>
        <Input
          {...register("productLink", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.productLink?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>인원수</InputTitle>
        <Input
          {...register("maxPeople", {
            required: "필수 정보입니다.",
          })}
        />
        <ErrorMessage>{errors?.maxPeople?.message}</ErrorMessage>
      </InputDiv>
      <InputDiv>
        <InputTitle>공구 마감 날짜</InputTitle>
        <Input
          {...register("deadline", {
            required: "필수 정보입니다.",
          })}
          type="datetime-local"
        />
        <ErrorMessage>{errors?.deadline?.message}</ErrorMessage>
      </InputDiv>

      <Button>생성하기</Button>
    </MoimForm>
  );
}

const MoimForm = styled.form`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 68px;
  margin: 0 auto;
  max-width: 680px;
`;

const FormTitle = styled.h2`
  font-weight: bold;
  font-size: 32px;
  margin: 55px;
`;

const InputDiv = styled.div`
  margin-bottom: 20px;
`;

const InputTitle = styled.div`
  text-align: left;
  font-size: 14px;
  font-weight: bold;
`;

const Input = styled.input`
  margin-top: 5px;
  margin-bottom: 1px;
  width: 400px;
  height: 45px;
  border-top: none;
  border-left: none;
  border-right: none;
  border-width: 1px;
`;

const Calendar = styled.input`
  display: row;
  margin-top: 5px;
  margin-bottom: 1px;
  width: 200px;
  height: 45px;
  border-top: none;
  border-left: none;
  border-right: none;
  border-width: 1px;
`;

const ErrorMessage = styled.p`
  text-align: left;
  font-size: 11px;
  color: #ff3f34;
`;

const Button = styled.button`
  margin-top: 35px;
  border-radius: 10px;
  border: none;
  width: 400px;
  height: 55px;
  font-size: 18px;
  font-weight: bold;
  background-color: #1e272e;
  color: white;
  cursor: pointer;
`;

export default CreateMoim;